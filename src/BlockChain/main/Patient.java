package BlockChain.main;

public record Patient(String Name, int age, int height, int weight, boolean sex, int bloodPressure, int pulse,
                      long ID) {
    @Override
    public String toString() {
        return "Patient{" +
                "Name='" + Name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                ", sex=" + (sex ? "Male" : "Female") +
                ", bloodPressure=" + bloodPressure +
                ", pulse=" + pulse +
                ", ID=" + ID +
                '}';
    }
}
