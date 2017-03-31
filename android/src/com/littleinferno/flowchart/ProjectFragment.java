package com.littleinferno.flowchart;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.badlogic.gdx.backends.android.AndroidFragmentApplication;
import com.littleinferno.flowchart.function.Function;
import com.littleinferno.flowchart.function.FunctionDetailsFragment;
import com.littleinferno.flowchart.util.Details;
import com.littleinferno.flowchart.variable.Variable;

public class ProjectFragment extends AndroidFragmentApplication {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return initializeForView(new Application(new VariableDetailsHelper(getFragmentManager()), new FunctionDetailsHelper(this)));
    }

    private static class VariableDetailsHelper extends Details.VariableHelper {

        private FragmentManager manager;

        VariableDetailsHelper(FragmentManager manager) {
            this.manager = manager;
        }


        @Override
        public void call(Variable variable) {
            VariableDetailsFragment v = new VariableDetailsFragment();
            v.setVariable(variable);
            v.show(manager, "vdd");
        }
    }

    private static class FunctionDetailsHelper extends Details.FunctionHelper {

        private ProjectFragment projectFragment;

        FunctionDetailsHelper(ProjectFragment projectFragment) {
            this.projectFragment = projectFragment;
        }

        @Override
        public void call(Function fun) {
            FunctionDetailsFragment v = new FunctionDetailsFragment();
            v.setFunction(fun);
            v.show(projectFragment.getFragmentManager(), "fdd");
        }
    }
}
