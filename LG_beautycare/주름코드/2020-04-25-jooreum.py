import numpy as np
import cv2
from PIL import Image
import glob
import os.path

#jpg->png convert
files = glob.glob('*.jpg')
for file in files:
    if not os.path.isdir(file):
        converted = file.replace('.jpg', '.png')
        os.rename(file, converted)

def main():
    img = Image.open('ttest2.png')
    img = img.resize((512, 512))
    img.save('resized.png')
    img = cv2.imread('resized.png')
    cv2.imshow("origin", img)
    
    B, G, R = cv2.split(img)  # B-Space 추출
    cv2.imshow("B-Space", B)                        #흑백 이미지 출력 B-space
    
    blur = cv2.bilateralFilter(B, 15, 75, 75)  # Bilateral Filter 적용 -> 노이즈 제거
    cv2.imshow("BilateralFiler", blur)              #노이즈 제거 출력
    
    hist = cv2.equalizeHist(blur)   #Histogram equlization -> 명암 대비
    cv2.imshow("Histogram Equalization", hist)      #명암 대비 출력
    
    adth = cv2.adaptiveThreshold(hist, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 71, 0)   #Adaptive Threshold -> 주름 의심영역
    cv2.imshow("Adatpive Threshold", adth)          #주름 의심영역 출력
    
    edges = cv2.Canny(hist, 50, 200,apertureSize = 3)
    
    lines = cv2.HoughLinesP(edges, 1, np.pi / 180, 100, minLineLength=30, maxLineGap=10)    #선형 검출 알고리즘
    
    for line in lines:
        x1, y1, x2, y2 = line[0]
        cv2.line(img, (x1,y1), (x2,y2), (0,255,0), 1)
        
    
    
    cv2.imshow("noline", img)
    cv2.imshow("Hough line Transform",img)          #선형 검출 이걸 수정하긴 해야함
    cv2.waitKey(0)
    
main()