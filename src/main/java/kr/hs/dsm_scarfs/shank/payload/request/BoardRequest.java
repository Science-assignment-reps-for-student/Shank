package kr.hs.dsm_scarfs.shank.payload.request;

import lombok.Getter;

@Getter
public class BoardRequest {

    private String title;

    private String content;

    private byte image;
}
