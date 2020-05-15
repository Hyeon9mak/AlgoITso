import numpy as np
import cv2
from PIL import Image

img = cv2.imread("test_j.png")
img2 = img
(B, G, R) = cv2.split(img)  # B-Space 추출

blur = cv2.bilateralFilter(B, 20, 75, 75)  # Bilateral Filter 적용 -> 노이즈 제거
# Parameters:	
# src – 8-bit, 1 or 3 Channel image
# d – filtering시 고려할 주변 pixel 지름
# sigmaColor – Color를 고려할 공간. 숫자가 크면 멀리 있는 색도 고려함.
# sigmaSpace – 숫자가 크면 멀리 있는 pixel도 고려함.

hist = cv2.equalizeHist(blur)   #Histogram equlization -> 명암 대비

adth = cv2.adaptiveThreshold(hist, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 21, 5)   #Adaptive Threshold -> 주름 의심영역

lines = cv2.HoughLinesP(adth, 1, np.pi / 180, 140, 30, 10)    #선형 검출 알고리즘



for line in lines:
    x1, y1, x2, y2 = line[0]
    cv2.line(img, (x1, y1), (x2, y2), (0, 255, 0), 5)       #선형 검출 알고리즘 표시 단계 RGB 초록색

hsv = cv2.cvtColor(img, cv2.COLOR_BGR2HSV)
Gmask = cv2.inRange(hsv, np.array([35, 80, 80]), np.array([80, 255,255]))
G = cv2.bitwise_and(img, img, mask=Gmask)

G = ~G
cv2.imshow('Gmask', G)

height, width, channel = G.shape
pink = Image.new("RGB", (width, height), (255, 0, 255))
pink.save("sample_png.png")
P = cv2.imread("sample_png.png")
cv2.imshow('pink', P)



sub_G = cv2.subtract(G, P)
cv2.imshow('sub_G', sub_G)

#cv2.imshow("Original image", img)           #원본 이미지 출력
#cv2.imshow("B-Space", B)                        #흑백 이미지 출력 B-space
#cv2.imshow("BilateralFiler", blur)              #노이즈 제거 출력
#cv2.imshow("Histogram Equalization", hist)      #명암 대비 출력
#cv2.imshow("Adatpive Threshold", adth)          #주름 의심영역 출력
#cv2.imshow("Hough line Transform",img)          #선형 검출 이걸 수정하긴 해야함
cv2.waitKey(0)
