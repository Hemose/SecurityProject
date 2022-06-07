package BlockChain.main;

public class Test {
    public static void main(String args[]) throws Exception {
//        //Creating a Signature object
//        Signature sign = Signature.getInstance("SHA256withRSA");
//
//        //Creating KeyPair generator object
//        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
//
//        //Initializing the key pair generator
//        keyPairGen.initialize(2048);
//
//        //Generating the pair of keys
//        KeyPair pair = keyPairGen.generateKeyPair();
//
//        //Creating a Cipher object
//        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
//
//        //Initializing a Cipher object
//        cipher.init(Cipher.ENCRYPT_MODE, pair.getPublic());
//
//        //Adding data to the cipher
//        byte[] input = "Welcome to Tutorialspoint".getBytes();
//        cipher.update(input);
//
//        //encrypting the data
//        byte[] cipherText = cipher.doFinal();
//        System.out.println(new String(cipherText, "UTF8"));
//
//        cipher.init(Cipher.DECRYPT_MODE, pair.getPrivate());
//        cipher.update(cipherText);
//
//        byte[] decryptedText = cipher.doFinal();
//        System.out.println(new String(decryptedText, "UTF8"));

//        Clinic clinic = new Clinic("ss");
//        String a = "Tria";
//        while (a.length() < 16) a += "s";
//        String enc = String.valueOf(Base64.getDecoder().decode(a));
//        System.out.println("This is Enc " + enc);

//        String dec = clinic.getAES().do_AESDecryption(Base64.getDecoder().decode(enc));
//        System.out.println("This is Dec " + dec);
        Clinic c1 = new Clinic("Dr. Ahmed"), c2 = new Clinic("Dr. Ashraf");
//
        long p1ID = c1.registerPatient("Hemose", 21, 180, 85, true, 90, 101);
        long p2ID = c2.registerPatient("Mona", 18, 165, 70, false, 85, 90);
//
        System.out.println(c1.getPatientList(p1ID));
        System.out.println(c2.getPatientList(p2ID));

//        c1.AddRecord();
    }
}
