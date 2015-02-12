package org.celllife.hpv;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import org.javarosa.core.model.FormDef;
import org.javarosa.core.model.FormIndex;
import org.javarosa.core.model.data.StringData;
import org.javarosa.core.model.instance.TreeElement;
import org.javarosa.core.model.instance.TreeReference;
import org.javarosa.xform.parse.XFormParser;
import org.odk.collect.android.R;
import org.odk.collect.android.application.Collect;
import org.odk.collect.android.exception.JavaRosaException;
import org.odk.collect.android.logic.FormController;
import org.odk.collect.android.utilities.FileUtils;

import android.content.res.AssetManager;
import android.content.res.Resources;

/**
 * Useful functions for the DoH HPV app
 */
public class HPVUtils {
    
    /**
     * Loads the data from the saved School Login form and inserts it in the current form
     * @throws JavaRosaException if any errors occur
     */
    public static void loadSchoolLoginData() throws JavaRosaException {
        File loginFormDataFile = new File(getSchoolLoginFormDataFileName());
        if (loginFormDataFile.exists()) {
            byte[] fileBytes = FileUtils.getFileAsBytes(loginFormDataFile);
            TreeElement savedRoot = XFormParser.restoreDataModel(fileBytes, null).getRoot();
            setFormData(savedRoot, HPVConsts.HPV_FORM_BINDING_EMIS_NUMBER);
            setFormData(savedRoot, HPVConsts.HPV_FORM_BINDING_SCHOOL_NAME);
            setFormData(savedRoot, HPVConsts.HPV_FORM_BINDING_SCHOOL_REP);
            setFormData(savedRoot, HPVConsts.HPV_FORM_BINDING_SCHOOL_REP_CONTACT);
            setFormData(savedRoot, HPVConsts.HPV_FORM_BINDING_SCHOOL_GPS);
        } else {
            // sorry for this, I am tired.
            throw new JavaRosaException(new RuntimeException("No School Login Form data file found ("+loginFormDataFile.getAbsolutePath()+")."));
        }
    }
    
    private static void setFormData(TreeElement savedRoot, String binding) throws JavaRosaException {
        FormController formController = Collect.getInstance().getFormController();
        FormDef formDef = formController.getFormDef();
        TreeElement question = savedRoot.getChild(binding, TreeReference.DEFAULT_MUTLIPLICITY);
        if (question != null) {
            FormIndex index = formController.getIndexFromXPath(getXPath(formDef, binding));
            if (index != null) {
                formController.answerQuestion(index, question.getValue());
            }
        }
    }

    /**
     * Gets the name of the School Login Form Xform XML document
     * @return
     */
    public static File getSchoolLoginFormFile() {
        String path = Collect.FORMS_PATH + File.separator + HPVConsts.HPV_SCHOOL_LOGIN_FORM_NAME + ".xml";
        return new File(path); 
    }
    
    /**
     * Gets the name of the School Emis CSV document
     * @return
     */
    public static File getSchoolEmisCSVFile() {
        String path = Collect.FORMS_PATH + File.separator + HPVConsts.HPV_SCHOOL_LOGIN_FORM_NAME + "-media" + File.separator + HPVConsts.SCHOOL_EMIS_CSV_FILENAME;
        return new File(path); 
    }
    
    /**
     * Gets the name of the file (including path) for the school login data XML file
     * @return
     */
    public static String getSchoolLoginFormDataFileName() {
        return Collect.INSTANCES_PATH + File.separator + HPVConsts.HPV_SCHOOL_LOGIN_DATA_FILENAME;
    }
    
    /**
     * Cleanup of assets copied to the device on the event of an update
     * @throws IOException
     */
    public static void cleanupAssets() throws IOException {
        getSchoolLoginFormFile().delete();
        File mediaDirectory = getSchoolEmisCSVFile().getParentFile();
        if (mediaDirectory != null && mediaDirectory.exists()) {
            for (File f : mediaDirectory.listFiles()) {
                f.delete();
            }
        }
        mediaDirectory.delete();
    }
    
    /**
     * Copies the School Login Form XML file and School EMIS CSV document to the disk (if it doesn't already exist.
     * @throws IOException
     */
    public static void initialiseSchoolLoginAssets() throws IOException {
        copyFileFromAssertToDevice(HPVConsts.HPV_SCHOOL_LOGIN_FORM_FILENAME, getSchoolLoginFormFile());
        copyFileFromAssertToDevice(HPVConsts.SCHOOL_EMIS_CSV_FILENAME, getSchoolEmisCSVFile());
    }
    
    private static void copyFileFromAssertToDevice(String asset, File dest) throws IOException {
        if (!dest.exists()) {
            if (!dest.getParentFile().exists()) {
                // create parent directories where necessary
                dest.getParentFile().mkdir();
            }
            Resources resources = Collect.getInstance().getResources();
            AssetManager assetManager = resources.getAssets();

            InputStream in = assetManager.open(asset);
            try {
                FileOutputStream out = new FileOutputStream(dest);
                try {
                    byte buf[] = new byte[4096];
                    int len;
                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    out.flush();
                } finally {
                    try {
                        out.close();
                    } catch (IOException e) { }
                }
            } finally {
                try {
                    in.close();
                } catch (IOException e) { }
            }
        }
    }

    /**
     * Gets the name of the learner from the entered data.
     * 
     * @param formDef FormDef xform definition
     * @param formRootElement TreeElement containing the captured data
     * @return String learner full name
     */
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
            fullName.append(Collect.getInstance().getResources().getString(R.string.HPV_unknown_learner));
        }

        return fullName.toString();
    }

    /**
     * Gets the xpath for the specified question
     * @param formDef FormDef xform definition
     * @param questionBinding String question binding
     * @return String xpath
     */
    public static String getXPath(FormDef formDef, String questionBinding) {
        String formBinding = formDef.getMainInstance().getRoot().getName();
        return "question./"+formBinding+"/"+questionBinding+"[1]";
    }
}
