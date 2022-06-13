package BlockChain.main;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Clinic {
    private static long id;
    public final String delm = "-@#!";
    private Long ClinicId;
    static String name;
    private KeyPair keyPair;
    private Symmetric AES;
    HashMap<Long, Integer> lastBlockAddress;
    Controller controller;

    public Clinic(String name) throws Exception {

        lastBlockAddress = new HashMap<>();
        this.name = name;
        controller = Controller.getController();
//        controller.RegisterClinic()

        //Creating KeyPair generator object
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");

        //Initializing the key pair generator
        keyPairGen.initialize(2048);

        //Generating the pair of keys
        keyPair = keyPairGen.generateKeyPair();

//        AES = new Symmetric();
        AES = new Symmetric();
        ClinicId = controller.registerClinic(keyPair.getPublic());
    }

    public Symmetric getAES() {
        return AES;
    }

    public void setAES(Symmetric AES) {
        this.AES = AES;
    }

    public String sign(String msg)
            throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
        return Base64.encodeBase64String(cipher.doFinal(msg.getBytes("UTF-8")));
    }

    public String decSigned(String msg)
            throws Exception {

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, keyPair.getPublic());
        return new String(cipher.doFinal(Base64.decodeBase64(msg)), "UTF-8");
    }


    public long registerPatient(String Name, int age, int height, int weight, boolean sex, int bloodPressure, int pulse) throws Exception {

        Patient cur = new Patient(Name, age, height, weight, sex, bloodPressure, pulse, id++);
        int pos = buildTransaction(cur.ID() + delm + cur.toString(), cur.ID());
        lastBlockAddress.put(cur.ID(), pos);
        return cur.ID();
    }

    public int buildTransaction(String data, long pId) throws Exception {
        System.out.println("Building transaction for Clinic " + ClinicId + " For Patient " + pId + " Data: " + data);
        StringBuilder sb = new StringBuilder(data);
        sb.append(delm);// some separation value
        if (lastBlockAddress.containsKey(pId))
            sb.append(lastBlockAddress.get(pId));
        else sb.append(-1);

        String sEnc = AES.encrypt(sb.toString());
        String hashed = StringUtil.applySha256(sEnc);
        String hashedSigned = sign(hashed);

        return controller.addTransaction(sEnc, ClinicId, hashedSigned);
    }

    public void addRecord(long pId, String data) throws Exception {

        int pos = buildTransaction(pId + delm + data, pId);

        lastBlockAddress.put(pId, pos);
    }

    public ArrayList<String> getPatientList(long pId) throws Exception {
        if (!lastBlockAddress.containsKey(pId))
            return new ArrayList<>();
        System.out.println("Getting data in Clinic" + ClinicId + " for patient " + pId);
        ArrayList<String> records = new ArrayList<>();

        int BlockId = lastBlockAddress.get(pId);
//        System.out.println(BlockId);
        while (BlockId != -1) {
            ArrayList<String> curBlock = controller.getBlockData(BlockId);
            int mnId = BlockId;
            for (int i = curBlock.size() - 1; i >= 0; i--) {
                String s = curBlock.get(i);
                System.out.println("Checking transaction " + s);
                //check the data and separate it.
                String decrypted;
                try {
                    decrypted = AES.decrypt(s);
                    System.out.println("Transaction is for this Clinic " + decrypted);
                    String[] tokens = decrypted.split(delm);

                    long id = Long.parseLong(tokens[0]);
                    String data = tokens[1];
                    int nxtID = Integer.parseInt(tokens[2]);


                    System.out.println("The patient ID is: " + id + " Data: " + data + " NextBlock: " + nxtID);
                    if (id != pId)
                        continue;
                    records.add(data);
                    mnId = Math.min(nxtID, mnId);
                } catch (Exception e) {

                }


            }
            BlockId = mnId;
        }
        return records;
    }


}
