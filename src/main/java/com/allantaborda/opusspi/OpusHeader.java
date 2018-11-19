package com.allantaborda.opusspi;

import com.allantaborda.ooogg.OggPackable;
import com.allantaborda.ooogg.OggPacket;
import com.allantaborda.ooogg.OggUtils;
import javax.sound.sampled.AudioFormat;

public class OpusHeader implements OggPackable{
	private byte channels;
	private short preSkip;
	private int inputSampleRate;
	private short outputGain;
	private byte channelMappingFamily;
	private byte streamCount;
	private byte coupledCount;
	private byte[] channelMapping;

	public OpusHeader(OggPacket packet){
		if(packet.getSize() > 18 && packet.headerMatches("OpusHead") && packet.getContent()[8] == 0x01){
			channels = packet.getContent()[9];
			preSkip = OggUtils.getShortFromByteArray(packet.getContent(), 10);
			inputSampleRate = OggUtils.getIntFromByteArray(packet.getContent(), 12);
			outputGain = OggUtils.getShortFromByteArray(packet.getContent(), 16);
			channelMappingFamily = packet.getContent()[18];
			if(packet.getSize() > 20){
				streamCount = packet.getContent()[19];
				coupledCount = packet.getContent()[20];
				channelMapping = new byte[packet.getSize() - 21];
				System.arraycopy(packet.getContent(), 21, channelMapping, 0, channelMapping.length);
			}
		}else channels = -1;
	}

	public OpusHeader(AudioFormat fmt){
		this((byte) fmt.getChannels(), (short) 0, (int) fmt.getSampleRate(), (short) 0, (byte) 0);
	}

	public OpusHeader(byte channels, short preSkip, int inputSampleRate, short outputGain, byte channelMappingFamily){
		this.channels = channels;
		this.preSkip = preSkip;
		this.inputSampleRate = inputSampleRate;
		this.outputGain = outputGain;
		this.channelMappingFamily = channelMappingFamily;
	}

	public byte getChannels(){
		return channels;
	}

	public short getPreSkip(){
		return preSkip;
	}

	public int getInputSampleRate(){
		return inputSampleRate;
	}

	public short getOutputGain(){
		return outputGain;
	}

	public byte getChannelMappingFamily(){
		return channelMappingFamily;
	}

	public byte getStreamCount(){
		return streamCount;
	}

	public byte getCoupledCount(){
		return coupledCount;
	}

	public byte[] getChannelMapping(){
		return channelMapping;
	}

	public boolean isValid(){
		boolean valid = channels > 0;
		if(channelMappingFamily != 0 && (streamCount == 0 || coupledCount > streamCount || channelMapping == null || channelMapping.length != channels)) valid = false;
		return valid;
	}

	public OggPacket toOggPacket(){
		byte[] content = new byte[channelMapping == null ? 19 : 21 + channelMapping.length];
		for(int c = 0; c < "OpusHead".length(); c++) content[c] = (byte) "OpusHead".charAt(c);
		content[8] = 0x01;
		content[9] = channels;
		System.arraycopy(OggUtils.getByteArrayFromShort(preSkip), 0, content, 10, 2);
		System.arraycopy(OggUtils.getByteArrayFromInt(inputSampleRate), 0, content, 12, 4);
		System.arraycopy(OggUtils.getByteArrayFromShort(outputGain), 0, content, 16, 2);
		content[18] = channelMappingFamily;
		if(channelMapping != null){
			content[19] = streamCount;
			content[20] = coupledCount;
			System.arraycopy(channelMapping, 0, content, 21, channelMapping.length);
		}
		return new OggPacket(content);
	}
}