package teamyj.dev.hrd_final_project.data_system;

import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.BUSINESS_NAME;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.BUSINESS_STATUS;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.EMERGENCY_LATITUDE;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.EMERGENCY_LONGITUDE;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.STREET_ADDRESS;
import static teamyj.dev.hrd_final_project.data_system.EmergencyDrugDBOpenHelper.TABLE_EMERGENCY_DRUG;

import android.content.ContentValues;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class EmergencyDrugCreate {
    private static final String EMERGENCY_DRUG_FILE = "emergency_drug/emergency_drug_final.csv";
    private EmergencyDrugDBOpenHelper emergencyDrugDBOpenHelper;
    private BufferedReader bufferedReader;

    /**--- Emergency Drug Create --- */
    public void getEmergencyDrug(AssetManager assetManager, EmergencyDrugDBOpenHelper emergencyDrugDBOpenHelper) {
        this.emergencyDrugDBOpenHelper = emergencyDrugDBOpenHelper;
        long time = System.currentTimeMillis();

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(assetManager.open(EMERGENCY_DRUG_FILE)));
            String[] buffer;
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                processingEmergencyData(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void processingEmergencyData(String emergency_info) {
        String[] preprocessing = emergency_info.split("\"");
        String[] temp;
        String status  = (temp = preprocessing[0].split(","))[0];
        String[] data;
        String street_addr;
        String name;
        int index;

        if(preprocessing.length == 1) {
            data = preprocessing[0].split(",");
            street_addr = data[1];
            name = data[2];
            index = 3;
        } else if(preprocessing.length == 3) {
            data = preprocessing[2].split(",");

            // addr에 ,가 들어간 경우
            if(temp.length == 1) {
                street_addr = preprocessing[1];
                name = data[1];
                index = 2;
            } else {    // 매장 이름에 ,가 들어간 경우
                street_addr = temp[1];
                name = preprocessing[1];
                index = 1;
            }
        } else {    // length == 5
            street_addr = preprocessing[1];
            name = preprocessing[3];
            data = preprocessing[4].split(",");
            index = 1;
        }

        addEmergencyDrugData(data, status, street_addr, name, index);
    }

    private void addEmergencyDrugData(String[] data, String status, String street_address, String name, int index) {
        SQLiteDatabase db = emergencyDrugDBOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BUSINESS_STATUS, status);
        values.put(STREET_ADDRESS, street_address);
        values.put(BUSINESS_NAME, name);

        values.put(EMERGENCY_LATITUDE, data[index++]);
        values.put(EMERGENCY_LONGITUDE, data[index]);

        db.insertWithOnConflict(TABLE_EMERGENCY_DRUG, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }
}
