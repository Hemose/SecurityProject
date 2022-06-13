package BlockChain.tests;

import BlockChain.main.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Tests {
    static String[] Clinics = {"El Noor", "El Safwa", "Dr. Ahmed", "Dr. Dewedar"};
    static String[] Patients = {"A", "B", "C", "D", "E", "F", "G"};
    static String[] Records = {"Go to another clinic", " come after 10 days", "You are good", "Stop studying security"
            , "Take a vacation", "You are healthy"};

    static int getRandom(int R) {

        return (int) ((Math.random() * R) + 1);
    }

    public static Patient registerRandomPatient(Clinic c) throws Exception {
        String Name = Patients[getRandom(Patients.length) - 1];
        int age = getRandom(10) + 15;
        int height = getRandom(40) + 150;
        int weight = getRandom(50) + 50;
        boolean sex = getRandom(2) == 1;
        int bloodPressure = getRandom(40) + 70;
        int pulse = getRandom(40) + 80;
        System.out.println("Patient Details " + Name + " " + age + " " + height + " " +
                weight + " " + sex + " " + bloodPressure + " " + pulse);
        long id = c.registerPatient(Name, age, height, weight, sex, bloodPressure, pulse);
        return new Patient(Name, age, height, weight, sex, bloodPressure, pulse, id);
    }

    public String getRandomRecord() {
        return Records[getRandom(Records.length) - 1];
    }

    public static String getRandomClinic() {
        return Clinics[getRandom(Clinics.length) - 1];
    }


    @Test
    public void patient1() throws Exception {

        Clinic c1 = new Clinic("Dr. Ahmed");
        long p1ID = c1.registerPatient("Hemose", 21, 180, 85, true, 90, 101);
        Patient p = new Patient("Hemose", 21, 180, 85, true, 90, 101, p1ID);
        ArrayList<String> record = c1.getPatientList(p1ID);

        assertEquals(record.size(), 1);
        assertEquals(record.get(record.size() - 1), p.toString());
    }

    @Test
    public void patient1MultiRecords() throws Exception {
        Clinic c1 = new Clinic("Dr. Ahmed");
        long p1ID = c1.registerPatient("Hemose", 21, 180, 85, true, 90, 101);
        Patient p = new Patient("Hemose", 21, 180, 85, true, 90, 101, p1ID);
        String visit = "You are healthy";

        c1.addRecord(p1ID, visit);
        ArrayList<String> record = c1.getPatientList(p1ID);

        assertEquals(record.size(), 2);
        assertEquals(record.get(record.size() - 1), p.toString());
        assertEquals(record.get(0), visit);

        ArrayList<String> visits = new ArrayList<>();
        visits.add(visit);
        int sz = getRandom(5);
        for (int i = 0; i < sz; i++) {
            visits.add(getRandomRecord());
            c1.addRecord(p1ID, visits.get(i + 1));
        }

        record = c1.getPatientList(p1ID);
        Collections.reverse(visits);
        for (int i = 0; i < visits.size(); i++) {
            assertEquals(record.get(i), visits.get(i));
        }
        assertEquals(record.get(record.size() - 1), p.toString());

    }

    @Test
    public void multiPatientMultiRecords1Clinc() throws Exception {
        Clinic c1 = new Clinic("Dr. Ahmed");

        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<ArrayList<String>> visits = new ArrayList<>();
        int numPatients = getRandom(5);

        for (int i = 0; i < numPatients; i++) {
            patients.add(registerRandomPatient(c1));
            patientRecords(patients, visits, i, c1);
        }

        for (int i = 0; i < numPatients; i++) {
            Collections.reverse(visits.get(i));
            ArrayList<String> records = c1.getPatientList(patients.get(i).ID());
            for (int j = 0; j < visits.get(i).size(); j++) {
                assertEquals(records.get(j), visits.get(i).get(j));
            }
            assertEquals(records.get(records.size() - 1), patients.get(i).toString());
        }
    }

    private ArrayList<Clinic> genClinics(int numClinics) throws Exception {
        ArrayList<Clinic> clinics = new ArrayList<>();
        for (int i = 0; i < numClinics; i++) {
            clinics.add(new Clinic(getRandomClinic()));
        }
        return clinics;
    }

    @Test
    public void multiPatientMultiRecordsMultiClinicsSmall() throws Exception {
//        Clinic c1 = new Clinic("Dr. Ahmed");
        int numClinics = getRandom(5);
        ArrayList<Clinic> clinics = genClinics(numClinics);

        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<ArrayList<String>> visits = new ArrayList<>();
        ArrayList<Integer> patientClinic = new ArrayList<>();
        int numPatients = getRandom(15);

        for (int i = 0; i < numPatients; i++) {
            int cId = getRandom(numClinics) - 1;
            Clinic selected = clinics.get(cId);
            patients.add(registerRandomPatient(selected));
            patientClinic.add(cId);
            patientRecords(patients, visits, i, selected);
        }

        checkDataValidity(clinics, patients, visits, patientClinic, numPatients);
    }

    private void patientRecords(ArrayList<Patient> patients, ArrayList<ArrayList<String>> visits, int i, Clinic selected) throws Exception {
        int recs = getRandom(5);
        visits.add(new ArrayList<>());
        for (int j = 0; j < recs; j++) {
            visits.get(i).add(getRandomRecord());
            selected.addRecord(patients.get(i).ID(), visits.get(i).get(j));
        }
    }

    @Test
    public void multiPatientMultiRecordsMultiClinicsBig() throws Exception {
//        Clinic c1 = new Clinic("Dr. Ahmed");
        int numClinics = getRandom(20);
        ArrayList<Clinic> clinics = genClinics(numClinics);


        ArrayList<Patient> patients = new ArrayList<>();
        ArrayList<ArrayList<String>> visits = new ArrayList<>();
        ArrayList<Integer> patientClinic = new ArrayList<>();
        int numPatients = getRandom(150);

        for (int i = 0; i < numPatients; i++) {
            int cId = getRandom(numClinics) - 1;
            Clinic selected = clinics.get(cId);
            patients.add(registerRandomPatient(selected));
            patientClinic.add(cId);
            int recs = getRandom(10);
            visits.add(new ArrayList<>());
            for (int j = 0; j < recs; j++) {
                visits.get(i).add(getRandomRecord());
                selected.addRecord(patients.get(i).ID(), visits.get(i).get(j));
            }
        }

        checkDataValidity(clinics, patients, visits, patientClinic, numPatients);
    }

    private void checkDataValidity(ArrayList<Clinic> clinics, ArrayList<Patient> patients, ArrayList<ArrayList<String>> visits, ArrayList<Integer> patientClinic, int numPatients) throws Exception {
        for (int i = 0; i < numPatients; i++) {
            Collections.reverse(visits.get(i));
            ArrayList<String> records = clinics.get(patientClinic.get(i)).getPatientList(patients.get(i).ID());
            for (int j = 0; j < visits.get(i).size(); j++) {
                assertEquals(records.get(j), visits.get(i).get(j));
            }
            assertEquals(records.get(records.size() - 1), patients.get(i).toString());
        }
    }

}