package org.concentus.spi;

import com.allantaborda.ooogg.OggPackable;
import com.allantaborda.ooogg.OggPacket;
import com.allantaborda.ooogg.Tags;
import com.allantaborda.ooogg.spi.OggAudioInputStream;
import com.allantaborda.ooogg.spi.OggFormatProvider;
import java.io.IOException;
import java.util.Map;
import javax.sound.sampled.AudioFileFormat.Type;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.concentus.OpusApplication;
import org.concentus.OpusEncoder;

public class OpusFormatProvider extends OggFormatProvider{
	public static final AudioFormat.Encoding OPUS_ENC = new AudioFormat.Encoding("OPUS");
	public static final Type OPUS_TYPE = new Type("OPUS", "opus");

	public OpusFormatProvider(){
		super(true, true);
	}

	public Type getType(){
		return OPUS_TYPE;
	}

	public AudioFormat.Encoding getEncoding(){
		return OPUS_ENC;
	}

	public String getEncoderName(){
		return "Concentus - Java implementation of the Opus audio codec";
	}

	public Tags getTags(){
		return new Tags("OpusTags");
	}

	public OggPackable getHeader(OggPacket packet){
		return new OpusHeader(packet);
	}

	public OGGAudioFileFormat getAudioFileFormat(OggPackable header, long length, Map<String, Object> afProps, Map<String, Object> affProps){
		OpusHeader h = (OpusHeader) header;
		afProps.put("channels", h.getChannels());
		afProps.put("preSkip", h.getPreSkip());
		afProps.put("inputSampleRate", h.getInputSampleRate());
		afProps.put("outputGain", h.getOutputGain());
		return new OGGAudioFileFormat(OPUS_TYPE, new AudioFormat(OPUS_ENC, 48000, AudioSystem.NOT_SPECIFIED, h.getChannels(), AudioSystem.NOT_SPECIFIED, AudioSystem.NOT_SPECIFIED, false, afProps), length, affProps);
	}

	public OggAudioInputStream getAudioInputStream(AudioFormat trgFormat, AudioInputStream srcStream) throws IOException{
		return new OpusAudioInputStream(trgFormat, srcStream);
	}

	public EncoderBox newEncoderBox(){
		return new OpusEncoderBox();
	}

	private class OpusEncoderBox extends EncoderBox{
		private OpusEncoder enc;
		private OpusHeader h;
		private byte[] opusPacket = new byte[500];

		public OggPackable getHeader(AudioFormat fmt){
			h = new OpusHeader(fmt);
			return h;
		}

		public void initEncoder() throws Exception{
			enc = new OpusEncoder(48000, h.getChannels(), OpusApplication.OPUS_APPLICATION_AUDIO);
			enc.setBitrate(96000);
			initPCMBuffer(1920, h.getChannels());
		}

		public void encode() throws Exception{
			byte[] z = new byte[enc.encode(getPCMBuffer(), 0, 960, opusPacket, 0, opusPacket.length)];
			System.arraycopy(opusPacket, 0, z, 0, z.length);
			setProcessedData(z);
			incrementGranulePosision(960L);
		}
	}
}