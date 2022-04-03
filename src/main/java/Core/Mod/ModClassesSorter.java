package Core.Mod;

import Core.Craftworld;
import Core.Exceptions.*;
import Core.Mod.New.ModImplement;
import Core.Mod.New.NewMod;
import HeadLibs.Helper.HStringHelper;
import HeadLibs.Pair;
import HeadLibs.Version.HVersionRange;

import java.util.*;

class ModClassesSorter {
    private static final List<Class<? extends ModImplement>> sortedMods = new ArrayList<>();
    private static final List<ModRequirementsException> exceptions = new ArrayList<>();
    private static final Collection<Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>, Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>, Pair<Boolean, Boolean>>>> modContainer = new HashSet<>();
    private static final Collection<Pair<Pair<Class<? extends ModImplement>, String>, Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>>> simpleModContainer = new HashSet<>();

    static List<Class<? extends ModImplement>> getSortedMods() {
        return sortedMods;
    }

    static List<ModRequirementsException> getExceptions() {
        return exceptions;
    }

    static void buildModContainer() {
        for (Class<? extends ModImplement> modClass: ModManager.getModList()) {
            NewMod mod = modClass.getAnnotation(NewMod.class);
            if (mod == null)
                continue;
            String modName = HStringHelper.noNull(mod.name().strip());
            String modVersion = HStringHelper.noNull(mod.version().strip());
            String modAvailable = HStringHelper.noNull(mod.availableCraftworldVersion().strip());
            HVersionRange availableCraftworldVersion = new HVersionRange(modAvailable);
            Set<Pair<Boolean, Pair<String, HVersionRange>>> after = new HashSet<>();
            Set<Pair<Boolean, Pair<String, HVersionRange>>> before = new HashSet<>();
            boolean afterAll = false;
            boolean beforeAll = false;
            String[] modRequirements = HStringHelper.noNull(HStringHelper.strip(mod.require().split(";")));
            for (String modRequirement: modRequirements) {
                int locationColon = modRequirement.indexOf(':');
                if (locationColon == -1) {
                    exceptions.add(new ModRequirementsFormatException(HStringHelper.concat("Need ':' to modify mod sort." +
                            " At class '", modClass, "', requirement: '", modRequirement, "'.")));
                    continue;
                }
                if (locationColon == modRequirement.length() - 1) {
                    exceptions.add(new ModRequirementsFormatException(HStringHelper.concat("Must have mod name." +
                            " At class '", modClass, "', requirement: '", modRequirement, "'.")));
                    continue;
                }
                String requirementModification = modRequirement.substring(0, locationColon);
                String requirementInformation = modRequirement.substring(locationColon + 1);
                int locationAt = requirementInformation.indexOf('@');
                String requirementName;
                HVersionRange versionRange;
                if (locationAt == -1 || locationAt == requirementInformation.length() - 1) {
                    if ("*".equals(requirementInformation)) {
                        switch (requirementModification) {
                            case "after" -> afterAll = true;
                            case "before" -> beforeAll = true;
                            default -> exceptions.add(new ModRequirementsFormatException(HStringHelper.concat("Unknown Modification in wildcard." +
                                    " At class: '", modClass, "', requirement: '", modRequirement, "', modification: '", requirementModification, "'.")));
                        }
                        continue;
                    }
                    requirementName = requirementInformation;
                    versionRange = new HVersionRange();
                }
                else {
                    requirementName = requirementInformation.substring(0, locationAt);
                    String requirementVersion = requirementInformation.substring(locationAt + 1);
                    versionRange = new HVersionRange(requirementVersion);
                }
                switch (requirementModification) {
                    case "after" -> after.add(Pair.makePair(false, Pair.makePair(requirementName, versionRange)));
                    case "require-after" -> after.add(Pair.makePair(true, Pair.makePair(requirementName, versionRange)));
                    case "before" -> before.add(Pair.makePair(false, Pair.makePair(requirementName, versionRange)));
                    case "require-before" -> before.add(Pair.makePair(true, Pair.makePair(requirementName, versionRange)));
                    default -> exceptions.add(new ModRequirementsFormatException(HStringHelper.concat("Unknown Modification." +
                            " At class: '", modClass, "', requirement: '", modRequirement, "', modification: '", requirementModification, "'.")));
                }
            }
            modContainer.add(Pair.makePair(Pair.makePair(modClass, Pair.makePair(modName, Pair.makePair(modVersion, availableCraftworldVersion))),
                    Pair.makePair(Pair.makePair(after, before), Pair.makePair(afterAll, beforeAll))));
        }
    }

    @SuppressWarnings("ConstantConditions")
    static void checkModContainer() {
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            //available in current Craftworld version
            if (mod.getKey().getValue().getValue().getValue().versionInRange(Craftworld.CURRENT_VERSION))
                exceptions.add(new WrongCraftworldVersionException(HStringHelper.concat("Current version '", Craftworld.CURRENT_VERSION, "' is not in range '", mod.getKey().getValue().getValue().getValue(), "'.",
                        " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //force requirement check
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getKey()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                    Pair<Boolean, Boolean>>> i : modContainer)
                        if (requireModName.equals(i.getKey().getValue().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModMissingException(HStringHelper.concat("Need mod '", requireModName, "'",
                                " for class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                }
            }
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getValue()) {
                if (requirements.getKey()) {
                    String requireModName = requirements.getValue().getKey();
                    boolean flag = true;
                    for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                            Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                    Pair<Boolean, Boolean>>> i : modContainer)
                        if (requireModName.equals(i.getKey().getValue().getKey())) {
                            flag = false;
                            break;
                        }
                    if (flag)
                        exceptions.add(new ModMissingException(HStringHelper.concat("Need mod '", requireModName, "'",
                                " for class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                }
            }
            //both after:* and before:*
            if (mod.getValue().getValue().getKey() && mod.getValue().getValue().getValue())
                exceptions.add(new ModRequirementsException(HStringHelper.concat("Both after:* and before:*",
                        " for class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //request after and before the same mod
            Collection<String> requestAfterModName = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getKey())
                requestAfterModName.add(requirements.getValue().getKey());
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getValue())
                if (requestAfterModName.contains(requirements.getValue().getKey()))
                    exceptions.add(new ModRequirementsException(HStringHelper.concat("Request after and before the same mod '", requirements.getValue().getKey(), "'.",
                            " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
            //mod version check
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getKey())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                        Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                Pair<Boolean, Boolean>>> b: modContainer)
                    if (requirements.getValue().getKey().equals(b.getKey().getValue().getKey())) {
                        if (requirements.getValue().getValue().versionInRange(b.getKey().getValue().getValue().getKey()))
                            exceptions.add(new ModVersionUnmatchedException(HStringHelper.concat("Have had mod '", b.getKey().getValue().getKey(), "@", b.getKey().getValue().getValue(), "' but need version '", requirements.getValue().getValue(), "'.",
                                    " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                    }
            for (Pair<Boolean, Pair<String, HVersionRange>> requirements: mod.getValue().getKey().getValue())
                for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                        Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                                Pair<Boolean, Boolean>>> b: modContainer)
                    if (requirements.getValue().getKey().equals(b.getKey().getValue().getKey())) {
                        if (requirements.getValue().getValue().versionInRange(b.getKey().getValue().getValue().getKey()))
                            exceptions.add(new ModVersionUnmatchedException(HStringHelper.concat("Have had mod '", b.getKey().getValue().getKey(), "@", b.getKey().getValue().getValue(), "' but need version '", requirements.getValue().getValue(), "'.",
                                    " At class: '", mod.getKey().getKey(), "' name: '", mod.getKey().getValue().getKey(), "'.")));
                    }
        }
    }
    
    @SuppressWarnings("ConstantConditions")
    static void toSimpleModContainer() {
        simpleModContainer.clear();
        for (Pair<Pair<Class<? extends ModImplement>, Pair<String, Pair<String, HVersionRange>>>,
                Pair<Pair<Set<Pair<Boolean, Pair<String, HVersionRange>>>, Set<Pair<Boolean, Pair<String, HVersionRange>>>>,
                        Pair<Boolean, Boolean>>> mod: modContainer) {
            Class<? extends ModImplement> modClass = mod.getKey().getKey();
            String modName = mod.getKey().getValue().getKey();
            Set<String> modRequireAfter = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionRange>> pair: mod.getValue().getKey().getKey())
                modRequireAfter.add(pair.getValue().getKey());
            Set<String> modRequireBefore = new HashSet<>();
            for (Pair<Boolean, Pair<String, HVersionRange>> pair: mod.getValue().getKey().getValue())
                modRequireBefore.add(pair.getValue().getKey());
            Pair<Boolean, Boolean> modRequireAll = mod.getValue().getValue();
            simpleModContainer.add(Pair.makePair(Pair.makePair(modClass, modName), Pair.makePair(Pair.makePair(modRequireAfter, modRequireBefore), modRequireAll)));
        }
        modContainer.clear(); //GC
    }

    private static final Collection<String> sortHasSearchedMods = new HashSet<>();

    @SuppressWarnings("ConstantConditions")
    static void sortMods() {
        sortedMods.clear();
        for (Pair<Pair<Class<? extends ModImplement>, String>,
                Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer) {
            sortHasSearchedMods.clear();
            addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                    mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
        }
    }

    @SuppressWarnings("ConstantConditions")
    private static void addSortMod(Class<? extends ModImplement> modClass, String modName,
                                   Collection<String> requireAfter, Collection<String> requireBefore, Pair<Boolean, Boolean> requireAll) {
        if (sortedMods.contains(modClass))
            return;
        if (sortHasSearchedMods.contains(modName))
            return;
        sortHasSearchedMods.add(modName);
        for (String after: requireAfter)
            for (Pair<Pair<Class<? extends ModImplement>, String>,
                    Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer)
                if (after.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        for (String before: requireBefore)
            for (Pair<Pair<Class<? extends ModImplement>, String>,
                    Pair<Pair<Set<String>, Set<String>>, Pair<Boolean, Boolean>>> mod: simpleModContainer)
                if (before.equals(mod.getKey().getValue())) {
                    addSortMod(mod.getKey().getKey(), mod.getKey().getValue(),
                            mod.getValue().getKey().getKey(), mod.getValue().getKey().getValue(), mod.getValue().getValue());
                    break;
                }
        int left = 0;
        for (int i = 0; i < sortedMods.size(); ++i)
            if (requireAfter.contains(sortedMods.get(i).getAnnotation(NewMod.class).name()))
                if (left == 0)
                    left = i + 1;
                else
                    left = Math.min(left, i + 1);
        int right = sortedMods.size();
        for (int i = 0; i < sortedMods.size(); ++i)
            if (requireBefore.contains(sortedMods.get(i).getAnnotation(NewMod.class).name()))
                if (right == sortedMods.size())
                    right = i;
                else
                    right = Math.max(right, i);
        if (left > right)
            exceptions.add(new ModRequirementsException(HStringHelper.concat("Mod sort error! left=", left, ", right=", right, ", sortedMod=", sortedMods,
                    " At class: '", modClass, "' name: '", modName, "'.")));
        else
            if (requireAll.getValue())
                sortedMods.add(left, modClass);
            else
                sortedMods.add(right, modClass);
    }

    static void gc() {
        exceptions.clear();
        modContainer.clear();
        simpleModContainer.clear();
    }
}
