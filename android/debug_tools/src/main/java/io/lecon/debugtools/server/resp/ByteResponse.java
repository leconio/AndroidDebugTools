package io.lecon.debugtools.server.resp;

/**
 * Created by spawn on 17-9-28.
 */

public class ByteResponse extends BaseResponse {

    private boolean isAssets;

    public ByteResponse(boolean isAssets,byte[] bytes) {
        this.bytes = bytes;
        this.isAssets = isAssets;
    }

    @Override
    public ResponseWrapper.ResponseType getType() {
        if (isAssets) {
            return ResponseWrapper.ResponseType.ASSETS;
        } else {
            return ResponseWrapper.ResponseType.FILE;
        }
    }
}
