package be.tomcools.protobuf;

import EXPERIMON.protos.BaseMessageOuterClass;
import EXPERIMON.protos.BaseMessageOuterClass.BaseMessage;
import EXPERIMON.protos.DistanceWalkedOuterClass;
import EXPERIMON.protos.DistanceWalkedOuterClass.DistanceWalked;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Examples {

    private DistanceWalked exampleObject = DistanceWalked.newBuilder()
            .setUserId("A User ID")
            .setDistance(0.12)
            .build();

    @Test
    public void givenSimpleDistanceWalkedProto_whenSerializedAndDeserialized_objectsAreEqual() throws InvalidProtocolBufferException {
        byte[] serialized = exampleObject.toByteArray();
        System.out.printf("Serialized: \n%s%n\n", Arrays.toString(serialized));

        DistanceWalked deserialized = DistanceWalked.parseFrom(serialized);
        System.out.printf("Deserialized: \n%s%n\n", deserialized);

        assertEquals(exampleObject, deserialized);
    }

    @Test
    public void givenExistingImage_whenUsingImageInBaseMessage_canSaveItAgain() throws IOException, IOException {
        File file = new File(this.getClass().getClassLoader().getResource("tenor.gif").getFile());

        byte[] imageBytes = Files.readAllBytes(file.toPath());
        BaseMessage message = BaseMessage.newBuilder()
                .setFilecontent(ByteString.copyFrom(imageBytes))
                .build();

        byte[] serializedMessage = message.toByteArray();

        BaseMessage deserialized = BaseMessage.parseFrom(serializedMessage);

        File newFile = new File("target/newFile.gif");
        Files.write(newFile.toPath(), deserialized.getFilecontent().toByteArray());
        System.out.println("Manually open the file to verify the GIF still works.");
    }
}
