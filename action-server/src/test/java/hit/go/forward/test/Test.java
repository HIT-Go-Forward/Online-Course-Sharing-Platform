package hit.go.forward.test;

import org.bson.types.ObjectId;

public class Test {
    public static void main(String[] args) {
        ObjectId objectId = new ObjectId();

        String hex = objectId.toHexString();
        System.out.println();
    }
}