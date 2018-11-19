package org.concentus.spi;

import com.allantaborda.ooogg.OggPacket;
import com.allantaborda.ooogg.spi.OggAudioInputStream;
import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;
import org.concentus.OpusPacketInfo;

public class OpusAudioInputStream extends OggAudioInputStream{
	private OpusDecoder dec;

	public OpusAudioInputStream(AudioFormat fmt, AudioInputStream is) throws IOException{
		super(fmt, is);
		try{
			dec = new OpusDecoder(48000, getChannels());
		}catch(OpusException e){
			throw new RuntimeException(e);
		}
	}

	protected boolean decode(OggPacket packet){
		int nrSamples = OpusPacketInfo.getNumSamples(dec, packet.getContent(), 0, packet.getSize());
		byte[] pcm = new byte[nrSamples * getChannels() * 2];
		try{
			dec.decode(packet.getContent(), 0, packet.getSize(), pcm, 0, nrSamples, false);
		}catch(OpusException e){
			return false;
		}
		createBuffer(pcm.length);
		for(int c = 0; c < pcm.length; c++) putInBuffer(pcm[c]);
		return true;
	}
}