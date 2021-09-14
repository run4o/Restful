package server;

/**
 * Module, storing data.
 *
 * @author Martin Trifonov
 * @version 1.0
 */
public class Module {

    private String subject;
    private String level;
    private String name;
    private boolean active;

    /**
     * Constructor. Creates an active module.
     *
     * @param subject
     * @param level
     * @param name
     */
    public Module(String subject, String level, String name) {
        this.subject = subject;
        this.level = level;
        this.name = name;
        this.active = true;
    }

    /**
     *
     */
    public Module() {
    }

    /**
     * Returns subject.
     *
     * @return subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Returns level.
     *
     * @return level.
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets level.
     *
     * @param level
     */
    public void setLevel(String level) {
        this.level = level;
    }

    /**
     * Returns name.
     *
     * @return name.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns if module is active.
     *
     * @return active status.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set active status.
     *
     * @param active
     */
    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return "\n" + "Module{" + " name=" + name + ", subject=" + subject + ", level=" + level + ", active=" + active + '}';
    }

}
