import sys
import os
import cv2
from PIL import Image

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

def main(user_name, img_name):
    #image file
    path = "UserData/"
    path_img = path +img_name
    img = cv2.imread(path_img)
    img = cv2.resize(img, (426,426))
    
    #graysclae with L space
    dst = cv2.cvtColor(img, cv2.COLOR_BGR2Lab)  # gray
    (L, a, b) = cv2.split(dst)

    #medianBlur
    median_img = algoITso_MedianBlur_Setup(L, 3, 1)
    median_img = algoITso_MedianBlur_Setup(median_img, 1, 3)  # median filtering
    
    #adaptive threshold
    thresh = cv2.adaptiveThreshold(median_img, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 21, 7)
    
    #blackhat
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (13, 13))
    dilate = cv2.dilate(thresh, kernel, iterations=1)
    erode = cv2.erode(dilate, kernel, iterations=1)
    blackhat = erode - thresh
    
    #labeling
    nlabels, labels, stats, centroids = cv2.connectedComponentsWithStats(blackhat)
    label_ = 0
    for i in range(2,nlabels):
        area = stats[i, cv2.CC_STAT_AREA] 
        left = stats[i, cv2.CC_STAT_LEFT]
        top = stats[i, cv2.CC_STAT_TOP]
        if area > 5 and area < 161:
            cv2.putText(img, '+',(left,top+10),cv2.FONT_HERSHEY_PLAIN, 1, (0,255,0), 1)
            label_+=1
    
    cv2.imwrite(str(path) + "pore_" + user_name + "_" + img_name, img)
    cv2.waitKey(0)
    return label_

main(sys.argv[1], sys.argv[2])
