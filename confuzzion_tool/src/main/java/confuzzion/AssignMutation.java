package confuzzion;

import soot.Body;
import soot.Local;
import soot.Scene;
import soot.SootMethod;
import soot.Type;
import soot.jimple.Jimple;

public class AssignMutation extends MethodMutation {
    public AssignMutation(RandomGenerator rand, SootMethod method)
            throws MutationException {
        super(rand, method);

        boolean allow_unsafe_assignment = ConfuzzionOptions.v().allow_unsafe_assignment;
        Body body = method.getActiveBody();
        if (body.getLocals().size() == 0) {
            throw new MutationException(AssignMutation.class,
                "No local inside body.");
        }
        Local localBefore = rand.randLocalRef(body.getLocals(), false);
        if (localBefore == null) {
            throw new MutationException(AssignMutation.class,
                "No useful reference inside body.");
        }
        Type typeBefore = localBefore.getType();
        // Pick a type from RandomGenerator
        Type typeAfter = rand.randRefType(method.getDeclaringClass().getName());
        if (!allow_unsafe_assignment || rand.nextBoolean()) {
            // Do a valid assignment to a common parent class
            typeAfter = typeBefore.merge(typeAfter, Scene.v());
        } //else: do a direct assignment between typeBefore and typeAfter
        Local localAfter =
            Jimple.v().newLocal("local" + rand.nextIncrement(), typeAfter);
        mutation.addLocal(localAfter);
        mutation.addUnit(Jimple.v().newAssignStmt(localAfter, localBefore));
    }

    // MODIF BENJA
    public AssignMutation(RandomGenerator rand, SootMethod bad_method, SootMethod good_method)
            throws MutationException {
        super(rand, bad_method);

        // MODIF BENJA
        //BodyMutation good_method_mutation = new BodyMutation(good_method.getActiveBody());

        boolean allow_unsafe_assignment = ConfuzzionOptions.v().allow_unsafe_assignment;
        Body body = method.getActiveBody();
        if (body.getLocals().size() == 0) {
            throw new MutationException(AssignMutation.class,
                    "No local inside body.");
        }
        Local localBefore = rand.randLocalRef(body.getLocals(), false);

        // MODIF BENJA
        //Body body_good_method = good_method.getActiveBody();
        //Local localBefore_good_method = rand.randLocalRef(body_good_method.getLocals(), false);

        if (localBefore == null) {
            throw new MutationException(AssignMutation.class,
                    "No useful reference inside body.");
        }
        Type typeBefore = localBefore.getType();
        // Pick a type from RandomGenerator
        Type typeAfter = rand.randRefType(method.getDeclaringClass().getName());
        if (!allow_unsafe_assignment || rand.nextBoolean()) {
            // Do a valid assignment to a common parent class
            typeAfter = typeBefore.merge(typeAfter, Scene.v());
        } //else: do a direct assignment between typeBefore and typeAfter
        Local localAfter =
                Jimple.v().newLocal("local" + rand.nextIncrement(), typeAfter);
        mutation.addLocal(localAfter);
        mutation.addUnit(Jimple.v().newAssignStmt(localAfter, localBefore));

        // MODIF BENJA
        //good_method_mutation.addLocal(localAfter);
        //good_method_mutation.addUnit(Jimple.v().newAssignStmt(localAfter, localBefore));
    }
}
