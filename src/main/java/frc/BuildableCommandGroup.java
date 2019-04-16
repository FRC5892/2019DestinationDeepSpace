package frc;

import java.util.function.Consumer;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 * A {@link CommandGroup} that accepts a {@link Consumer} whose {@link Consumer#accept accept()} method is immediately called with {@code this} as the argument.
 * 
 * @author Kai Page
 */
public class BuildableCommandGroup extends CommandGroup {
    public BuildableCommandGroup(Consumer<CommandGroup> lambda) {
        lambda.accept(this);
    }
}