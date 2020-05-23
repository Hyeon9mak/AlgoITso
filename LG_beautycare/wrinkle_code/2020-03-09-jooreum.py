import numpy as np
import cv2

img = cv2.imread("test_j.jpg")

(B, G, R) = cv2.split(img)  # B-Space 추출

blur = cv2.bilateralFilter(B, 9, 75, 75)  # Bilateral Filter 적용 -> 노이즈 제거

hist = cv2.equalizeHist(blur)   #Histogram equlization -> 명암 대비

adth = cv2.adaptiveThreshold(hist, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY, 21, 5)   #Adaptive Threshold -> 주름 의심영역

lines = cv2.HoughLinesP(adth, 1, np.pi / 180, 140, minLineLength=100, maxLineGap=10)    #선형 검출 알고리즘
cv2.imshow("Original image", img)           #원본 이미지 출력
for line in lines:
    x1, y1, x2, y2 = line[0]
    cv2.line(img, (x1, y1), (x2, y2), (0, 255, 0), 2)       #선형 검출 알고리즘 표시 단계 RGB 초록색

cv2.imshow("B-Space", B)                        #흑백 이미지 출력 B-space
cv2.imshow("BilateralFiler", blur)              #노이즈 제거 출력
cv2.imshow("Histogram Equalization", hist)      #명암 대비 출력
cv2.imshow("Adatpive Threshold", adth)          #주름 의심영역 출력
cv2.imshow("Hough line Transform",img)          #선형 검출 이걸 수정하긴 해야함
cv2.waitKey(0)
