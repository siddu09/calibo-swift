package utils;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.Singular;
import lombok.ToString;
import lombok.extern.jackson.Jacksonized;

import java.util.List;

@Getter
@Builder(toBuilder = true)
@Jacksonized
@ToString
public class FeatureData {

    @Singular private final List<String> phases;
    private final String name;
    private final String shortDescription;
    private final String description;
    private final String completionDate;

    @Builder.Default
    private final String status = "In QA";

    @Singular private final List<String> owners;
    @Singular private final List<String> teamMembers;
    @Singular private final List<String> userStories;

    public boolean hasPhases()       { return phases != null && !phases.isEmpty(); }
    public boolean hasOwners()       { return owners != null && !owners.isEmpty(); }
    public boolean hasTeamMembers()  { return teamMembers != null && !teamMembers.isEmpty(); }
    public boolean hasUserStories()  { return userStories != null && !userStories.isEmpty(); }
}