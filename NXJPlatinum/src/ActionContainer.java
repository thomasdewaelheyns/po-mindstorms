
import java.util.ArrayList;

/**
 *
 * @author MHGameWork
 */
public class ActionContainer {
    private static ArrayList<IAction> actions = new ArrayList<IAction>();
    public static void AddAction(IAction action)
    {
        actions.add(action);
    }
    public IAction[] GetActions()
    {
        return (IAction[]) actions.toArray();
    }
}
