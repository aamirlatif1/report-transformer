package com.astraia.transformer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TransformedResult {
    private final String fileName;
    private final String content;

}
