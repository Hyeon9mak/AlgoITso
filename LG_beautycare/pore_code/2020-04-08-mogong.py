import cv2
import numpy as np

def algoITso_MedianBlur(l):
    l_rows, l_cols = l.shape
    q = []
    for r in range(l_rows):
        for c in range(l_cols):
            q.append(l[r][c])

    q.sort()
    return q[len(q) // 2]


def algoITso_MedianBlur_Setup(src, KsizeX, KsizeY):
    dst = src
    src_rows, src_cols = src.shape
    Xr = KsizeX // 2
    Yr = KsizeY // 2

    for r in range(Yr, src_rows - Yr):
        for c in range(Xr, src_cols - Xr):
            window = src[r - Yr:(r - Yr) + KsizeY, c - Xr:(c - Xr) + KsizeX]
            dst[r, c] = algoITso_MedianBlur(window)

    return dst

def main():
    img = cv2.imread('test_img.jpeg')
    dst=cv2.cvtColor(img, cv2.COLOR_BGR2Lab)   #gray
    (L,a,b)=cv2.split(dst)

    median_img = algoITso_MedianBlur_Setup(L, 7, 3)
    median_img = algoITso_MedianBlur_Setup(median_img, 3, 7) #median filtering

    thresh = cv2.adaptiveThreshold(L, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 21, 7) #thresholding

    kernel=cv2.getStructuringElement(cv2.MORPH_ELLIPSE, (11,11))
    blackhat = cv2.morphologyEx(thresh, cv2.MORPH_BLACKHAT, kernel)  #blackhat

    cv2.imshow("Original image",img)
    cv2.imshow("Median filtering",median_img)
    cv2.imshow("Thresholding2", thresh)
    cv2.imshow("Blackhat",blackhat)
    cv2.waitKey(0)

main()

