package br.com.beeahead.smart_config.esptouch.protocol;

import br.com.beeahead.smart_config.esptouch.util.ByteUtil;

public class TouchData {
    private final byte[] mData;

    public TouchData(String string) {
        mData = ByteUtil.getBytesByString(string);
    }

    public TouchData(byte[] data) {
        if (data == null) {
            throw new NullPointerException("data can't be null");
        }
        mData = data;
    }

    public byte[] getData() {
        return mData;
    }
}
