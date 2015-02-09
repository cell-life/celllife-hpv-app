package org.celllife.hpv;

import java.util.Vector;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.data.StringData;
import org.javarosa.core.model.instance.TreeElement;
import org.odk.collect.android.R;

import android.content.res.Resources;

public class HPVUtils {

    public static String getFormName(FormDef formDef, TreeElement formRootElement) {
        StringBuilder fullName = new StringBuilder();
        Vector<TreeElement> nameElements = formRootElement.getChildrenWithName(HPVConsts.HPV_FORM_BINDING_LEARNER_NAME);
        if (nameElements.size() == 1) {
            StringData sa = (StringData)nameElements.get(0).getValue();
            if (sa != null) {
                fullName.append(sa.getValue());
            }
        }
        Vector<TreeElement> surnameElements = formRootElement.getChildrenWithName(HPVConsts.HPV_FORM_BINDING_LEARNER_SURNAME);
        if (surnameElements.size() == 1) {
            StringData sa = (StringData)surnameElements.get(0).getValue();
            if (sa != null) {
                if (fullName.length() > 0) {
                    fullName.append(" ");
                }
                fullName.append(sa.getValue());
            }
        }
        
        if (fullName.length() == 0) {
            fullName.append(Resources.getSystem().getString(R.string.HPV_unknown_learner));
        }

        return fullName.toString();
    }

    public static String getXPath(FormDef formDef, String questionBinding) {
        String formBinding = formDef.getMainInstance().getRoot().getName();
        return "question./"+formBinding+"/"+questionBinding+"[1]";
    }
}
