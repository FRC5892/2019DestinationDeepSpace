package frc;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * An in-place builder for  {@link CommandGroup}s. Convenient for one-off tests or excessively lazy autons.
 * 
 * @author Kai Page
 */
@Deprecated // in favor of BuildableCommandGroup
public class CommandGroupBuilder {
    private CommandGroup output = new CommandGroup();

    /**
     * @return the CommandGroup you've worked so hard to build.
     */
    public CommandGroup getOutput() {
        return output;
    }

    /**
     * Calls the corresponding method on the output CommandGroup.
     *
     * @return itself, so you can chain method calls
     */
    public CommandGroupBuilder addSequential(Command command) {
        output.addSequential(command);
        return this;
    }

    /**
     * Calls the corresponding method on the output CommandGroup.
     *
     * @return itself, so you can chain method calls
     */
    public CommandGroupBuilder addSequential(Command command, double timeout) {
        output.addSequential(command, timeout);
        return this;
    }

    /**
     * Calls the corresponding method on the output CommandGroup.
     *
     * @return itself, so you can chain method calls
     */
    public CommandGroupBuilder addParallel(Command command) {
        output.addParallel(command);
        return this;
    }

    /**
     * Calls the corresponding method on the output CommandGroup.
     *
     * @return itself, so you can chain method calls
     */
    public CommandGroupBuilder addParallel(Command command, double timeout) {
        output.addSequential(command, timeout);
        return this;
    }

    /**
     * Calls the corresponding method on the output CommandGroup.
     *
     * @return itself, so you can chain method calls
     */
    public CommandGroupBuilder setName(String name) {
        output.setName(name);
        return this;
    }
}
