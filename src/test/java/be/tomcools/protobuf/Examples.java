package be.tomcools.protobuf;

import EXPERIMON.protos.BaseMessageOuterClass.BaseMessage;
import EXPERIMON.protos.DistanceWalkedOuterClass.DistanceWalked;
import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class Examples {

    @Test
    public void givenSimpleDistanceWalkedProto_whenSerializedAndDeserialized_objectsAreEqual() throws InvalidProtocolBufferException {
        DistanceWalked exampleObject = DistanceWalked.newBuilder()
                .setUserId("A User ID")
                .setDistance(0.12)
                .build();

        byte[] serialized = exampleObject.toByteArray();
        System.out.printf("Serialized: \n%s%n\n", Arrays.toString(serialized));

        DistanceWalked deserialized = DistanceWalked.parseFrom(serialized);
        System.out.printf("Deserialized: \n%s%n\n", deserialized);

        assertThat(exampleObject).isEqualTo(deserialized);
    }

    @Test
    public void givenSomeMetadataAndByteArray_whenConstrucingBaseMessage_messageContainsThoseDetails() throws IOException {
        Map<String, String> metaMap = Map.of("filename", "SOME_FILE_NAME", "extension", "jpg");
        byte[] bytes = new byte[]{0, 8, -4};

        BaseMessage message = BaseMessage.newBuilder()
                .setFilecontent(ByteString.copyFrom(bytes))
                .putAllMetadata(metaMap)
                .build();

        assertThat(message.getFilecontent().toByteArray()).isEqualTo(bytes);
        assertThat(message.getMetadata()).isEqualTo(metaMap);
    }

    @Test
    public void givenSomeMetadataAndByteArray_whenSerializingAndDeserializing_messageIsSame() throws IOException {
        Map<String, String> metaMap = Map.of("filename", "SOME_FILE_NAME", "extension", "jpg");
        byte[] bytes = new byte[]{0, 8, -4};

        BaseMessage message = BaseMessage.newBuilder()
                .setFilecontent(ByteString.copyFrom(bytes))
                .putAllMetadata(metaMap)
                .build();

        byte[] serialized = message.toByteArray();

        BaseMessage deserialized = BaseMessage.parseFrom(serialized);

        assertThat(message).isEqualTo(deserialized);
    }

    @Test
    public void givenExistingImage_whenUsingImageInBaseMessage_canSaveItAgain() throws IOException {
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
