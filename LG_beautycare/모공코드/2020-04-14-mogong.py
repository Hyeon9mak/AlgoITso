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
    img = cv2.imread('al_test.png')
    cv2.imshow("Original image",img)
    
    dst=cv2.cvtColor(img, cv2.COLOR_BGR2Lab)   #gray
    (L,a,b)=cv2.split(dst)
    cv2.imshow("L space",L)
    
    median_img = algoITso_MedianBlur_Setup(L, 1, 3)
    median_img = algoITso_MedianBlur_Setup(median_img, 3, 1) #median filtering
    cv2.imshow("Median filtering",median_img)
    
    
    thresh = cv2.adaptiveThreshold(L, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 21, 7)
    cv2.imshow("Threshold",thresh)
    
    
    kernel = cv2.getStructuringElement (cv2.MORPH_RECT, (11,11))
    dilate = cv2.dilate(thresh, kernel,iterations = 1)
    erode = cv2.erode(dilate, kernel,iterations = 1)
    blackhat = erode - thresh
    cv2.imshow("black - hat",blackhat)
    
    nlabels, labels, stats, centroids = cv2.connectedComponentsWithStats(blackhat)

    for i in range(nlabels):

        if i < 2:
            continue
            
        area = stats[i, cv2.CC_STAT_AREA]
        center_x = int(centroids[i, 0])
        center_y = int(centroids[i, 1]) 
        left = stats[i, cv2.CC_STAT_LEFT]
        top = stats[i, cv2.CC_STAT_TOP]
        width = stats[i, cv2.CC_STAT_WIDTH]
        height = stats[i, cv2.CC_STAT_HEIGHT]


        if area > 7:
            cv2.rectangle(blackhat, (left, top), (left + width, top + height), (0, 255, 0), 1)
            cv2.circle(blackhat, (center_x, center_y), 5, (0, 0, 255), 1)
            cv2.rectangle(img, (left, top), (left + width, top + height), (0, 0, 255), 1)
            cv2.circle(img, (center_x, center_y), 5, (255, 0, 0), 1)


    cv2.imshow("result", blackhat)
    cv2.imshow("result2",img)
    cv2.waitKey(0)

main()