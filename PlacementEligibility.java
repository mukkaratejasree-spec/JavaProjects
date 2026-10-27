import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ============================================================================
 * Project Title : Placement Eligibility Decision Engine
 * Description   : A modular Java application utilizing OOP principles
 *                 (Encapsulation, Abstraction, Inheritance, Polymorphism)
 *                 to evaluate candidate eligibility for job openings.
 * ============================================================================
 */

// ============================================================================
// 1. ENCAPSULATION: Core Entities with private fields and public getters/setters
// ============================================================================

/**
 * Represents a student/candidate applying for placements.
 */
class Candidate {
    private String name;
    private double percentage;
    private int backlogs;
    private int graduationYear;
    private List<String> skills;

    // Default Constructor
    public Candidate() {
        this.skills = new ArrayList<>();
    }

    // Parameterized Constructor
    public Candidate(String name, double percentage, int backlogs, int graduationYear, List<String> skills) {
        this.name = name;
        this.percentage = percentage;
        this.backlogs = backlogs;
        this.graduationYear = graduationYear;
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
    }

    // Getters and Setters (Encapsulation)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public int getBacklogs() {
        return backlogs;
    }

    public void setBacklogs(int backlogs) {
        this.backlogs = backlogs;
    }

    public int getGraduationYear() {
        return graduationYear;
    }

    public void setGraduationYear(int graduationYear) {
        this.graduationYear = graduationYear;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills != null ? new ArrayList<>(skills) : new ArrayList<>();
    }
}

/**
 * Represents a job opening and its eligibility requirements.
 */
class Job {
    private String company;
    private String role;
    private double minPercentage;
    private int maxAllowedBacklogs;
    private int requiredGraduationYear;
    private List<String> requiredSkills;

    // Default Constructor
    public Job() {
        this.requiredSkills = new ArrayList<>();
    }

    // Parameterized Constructor
    public Job(String company, String role, double minPercentage, int maxAllowedBacklogs,
               int requiredGraduationYear, List<String> requiredSkills) {
        this.company = company;
        this.role = role;
        this.minPercentage = minPercentage;
        this.maxAllowedBacklogs = maxAllowedBacklogs;
        this.requiredGraduationYear = requiredGraduationYear;
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
    }

    // Getters and Setters (Encapsulation)
    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public double getMinPercentage() {
        return minPercentage;
    }

    public void setMinPercentage(double minPercentage) {
        this.minPercentage = minPercentage;
    }

    public int getMaxAllowedBacklogs() {
        return maxAllowedBacklogs;
    }

    public void setMaxAllowedBacklogs(int maxAllowedBacklogs) {
        this.maxAllowedBacklogs = maxAllowedBacklogs;
    }

    public int getRequiredGraduationYear() {
        return requiredGraduationYear;
    }

    public void setRequiredGraduationYear(int requiredGraduationYear) {
        this.requiredGraduationYear = requiredGraduationYear;
    }

    public List<String> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<String> requiredSkills) {
        this.requiredSkills = requiredSkills != null ? new ArrayList<>(requiredSkills) : new ArrayList<>();
    }
}

// ============================================================================
// 2. ABSTRACTION: Abstract base rule class defining contract for evaluation
// ============================================================================

abstract class EligibilityRule {
    protected String ruleName;

    public EligibilityRule(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getRuleName() {
        return ruleName;
    }

    /**
     * Abstract method to validate if a candidate passes this rule for a given job.
     * Implemented individually by concrete subclasses.
     */
    public abstract boolean validate(Candidate candidate, Job job);
}

// ============================================================================
// 3. INHERITANCE: Specific rules extending EligibilityRule
// ============================================================================

/**
 * Percentage Rule: Candidate Percentage >= Job Minimum Percentage
 */
class PercentageRule extends EligibilityRule {
    public PercentageRule() {
        super("Percentage");
    }

    @Override
    public boolean validate(Candidate candidate, Job job) {
        return candidate.getPercentage() >= job.getMinPercentage();
    }
}

/**
 * Backlog Rule: Candidate Backlogs <= Job Maximum Allowed Backlogs
 */
class BacklogRule extends EligibilityRule {
    public BacklogRule() {
        super("Backlogs");
    }

    @Override
    public boolean validate(Candidate candidate, Job job) {
        return candidate.getBacklogs() <= job.getMaxAllowedBacklogs();
    }
}

/**
 * Graduation Year Rule: Candidate Graduation Year == Job Required Graduation Year
 */
class GraduationYearRule extends EligibilityRule {
    public GraduationYearRule() {
        super("Graduation Year");
    }

    @Override
    public boolean validate(Candidate candidate, Job job) {
        return candidate.getGraduationYear() == job.getRequiredGraduationYear();
    }
}

/**
 * Skill Rule: Candidate possesses all required job skills.
 */
class SkillRule extends EligibilityRule {
    public SkillRule() {
        super("Skills");
    }

    @Override
    public boolean validate(Candidate candidate, Job job) {
        if (job.getRequiredSkills() == null || job.getRequiredSkills().isEmpty()) {
            return true;
        }
        if (candidate.getSkills() == null || candidate.getSkills().isEmpty()) {
            return false;
        }

        // Case-insensitive skill matching
        Set<String> candidateSkills = new HashSet<>();
        for (String skill : candidate.getSkills()) {
            if (skill != null) {
                candidateSkills.add(skill.trim().toLowerCase());
            }
        }

        for (String required : job.getRequiredSkills()) {
            if (required != null && !candidateSkills.contains(required.trim().toLowerCase())) {
                return false;
            }
        }

        return true;
    }
}

// ============================================================================
// 4. POLYMORPHISM: Engine executing rules polymorphically via base reference
// ============================================================================

class PlacementEngine {
    private List<EligibilityRule> rules;

    public PlacementEngine() {
        this.rules = new ArrayList<>();
        // Register default rules
        registerDefaultRules();
    }

    private void registerDefaultRules() {
        rules.add(new PercentageRule());
        rules.add(new BacklogRule());
        rules.add(new GraduationYearRule());
        rules.add(new SkillRule());
    }

    // Allows dynamic extension of rules (Open/Closed Principle)
    public void addRule(EligibilityRule rule) {
        rules.add(rule);
    }

    /**
     * Evaluates all rules polymorphically and displays the formatted summary report.
     */
    public boolean evaluate(Candidate candidate, Job job) {
        System.out.println("================================");
        System.out.println("PLACEMENT ELIGIBILITY RESULT");
        System.out.println("================================");
        System.out.printf("%-10s: %s%n", "Candidate", candidate.getName());
        System.out.printf("%-10s: %s%n", "Company", job.getCompany());
        System.out.printf("%-10s: %s%n", "Role", job.getRole());
        System.out.println();

        boolean isEligible = true;

        // Polymorphic invocation: rule.validate() calls the appropriate subclass method
        for (EligibilityRule rule : rules) {
            boolean passed = rule.validate(candidate, job);
            String status = passed ? "PASS" : "FAIL";
            System.out.printf("%-16s: %s%n", rule.getRuleName(), status);
            if (!passed) {
                isEligible = false;
            }
        }

        System.out.println();
        System.out.println("Final Result : " + (isEligible ? "ELIGIBLE" : "NOT ELIGIBLE"));
        System.out.println("================================");

        return isEligible;
    }
}

// ============================================================================
// 5. MAIN CLASS & EXECUTION
// ============================================================================

public class PlacementEligibility {

    public static void main(String[] args) {
        // Instantiate the Decision Engine
        PlacementEngine engine = new PlacementEngine();

        // 1. Sample Demonstration as specified in the prompt requirements
        Candidate rahul = new Candidate(
            "Rahul",
            78.5,             // Percentage: 78.5%
            0,                // Backlogs: 0
            2024,             // Graduation Year: 2024
            Arrays.asList("Java", "SQL", "Spring Boot", "HTML") // Candidate Skills
        );

        Job infosysJob = new Job(
            "Infosys",
            "JavaDeveloper",
            65.0,             // Minimum Percentage: 65.0%
            0,                // Maximum Allowed Backlogs: 0
            2024,             // Required Graduation Year: 2024
            Arrays.asList("Java", "SQL") // Required Skills
        );

        // Run evaluation on sample candidate and job
        engine.evaluate(rahul, infosysJob);

        // ------------------------------------------------------------------------
        // Optional: Interactive Mode
        // Uncomment the line below if you want to input details manually via console!
        // ------------------------------------------------------------------------
        // runInteractiveInput(engine);
    }

    /**
     * Helper method to accept Candidate and Job inputs dynamically from user console.
     */
    public static void runInteractiveInput(PlacementEngine engine) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n--- ENTER CANDIDATE DETAILS ---");
        System.out.print("Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Percentage (e.g. 75.5): ");
        double percentage = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Current Backlogs (e.g. 0): ");
        int backlogs = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Graduation Year (e.g. 2024): ");
        int gradYear = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Skills (comma-separated, e.g. Java, SQL, Spring): ");
        String skillsLine = scanner.nextLine().trim();
        List<String> skills = Arrays.stream(skillsLine.split(","))
                                    .map(String::trim)
                                    .filter(s -> !s.isEmpty())
                                    .collect(Collectors.toList());

        Candidate candidate = new Candidate(name, percentage, backlogs, gradYear, skills);

        System.out.println("\n--- ENTER JOB DETAILS ---");
        System.out.print("Company Name: ");
        String company = scanner.nextLine().trim();

        System.out.print("Role: ");
        String role = scanner.nextLine().trim();

        System.out.print("Minimum Required Percentage: ");
        double minPercentage = Double.parseDouble(scanner.nextLine().trim());

        System.out.print("Maximum Allowed Backlogs: ");
        int maxBacklogs = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Required Graduation Year: ");
        int reqGradYear = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Required Skills (comma-separated, e.g. Java, SQL): ");
        String reqSkillsLine = scanner.nextLine().trim();
        List<String> reqSkills = Arrays.stream(reqSkillsLine.split(","))
                                       .map(String::trim)
                                       .filter(s -> !s.isEmpty())
                                       .collect(Collectors.toList());

        Job job = new Job(company, role, minPercentage, maxBacklogs, reqGradYear, reqSkills);

        System.out.println();
        engine.evaluate(candidate, job);
    }
}
