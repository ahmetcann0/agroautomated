package model;

import org.python.util.PythonInterpreter;
import org.python.core.PyObject;

public class PredictUsingAI {
    private PythonInterpreter interpreter;
    private PyObject model;

    public PredictUsingAI() {
        // Initialize the Python interpreter
        interpreter = new PythonInterpreter();

        // Load the model
        interpreter.exec("import pickle\n" +
                        "with open('model.pkl', 'rb') as f:\n" +
                        "    model = pickle.load(f)");
        model = interpreter.get("model");
    }

    public Object predict(String[] data) {
        // Call the predict method of the model
        return model.invoke("predict", args);
    }
}
