import sys
import os
import numpy as np
import cv2
from PIL import Image

# Canny 알고리즘
def canny(img, low_threshold, high_threshold): 
    return cv2.Canny(img, low_threshold, high_threshold, apertureSize = 3)

# 선 그리기
def draw_lines(img, lines, color=[255, 255, 255], thickness=2): 
    for line in lines:
        for x1,y1,x2,y2 in line:
            cv2.line(img, (x1, y1), (x2, y2), color, thickness,cv2.FILLED)

# 허프 변환            
def hough_lines(img, rho, theta, threshold, min_line_len, max_line_gap): 
    lines = cv2.HoughLinesP(img, rho, theta, threshold, np.array([]), minLineLength=min_line_len, maxLineGap=max_line_gap)
    line_img = np.zeros((img.shape[0], img.shape[1], 3), dtype=np.uint8)
    draw_lines(line_img, lines)
    
    return line_img

def main(user_name, img_name):
    #imge file
    path = "UserData/"
    path_img = path + img_name
    img = cv2.imread(path_img)
    img = cv2.resize(img, (426,426))
    
    # B-Space 추출 -> 실제 피부색을 띄고 있기에 red, green으로는 구분 불가
    B, G, R = cv2.split(img)  
    
    # Bilateral Filter 적용 -> 잡음제거, intensity값과 위치정보 값을 모두 사용
    blur = cv2.bilateralFilter(B, 27, 71, 71)
    
    # Histogram equlization -> 명암 대비 영상
    hist = cv2.equalizeHist(blur)
    
    # Adaptive Threshold -> 확실한 주름 + 주름으로 의심되는 부분
    # adaptiveThreshold(원본 이미지, 조건값에 대한 픽셀값의 최대값, 주위 픽셀값 처리, 결과값 처리 방식, 
    #                   주위값의 픽셀을 얻을 window의 크기, C(주위 픽셀값 처리하고 마지막 C를 더함)의 값) )
    kernel = cv2.getStructuringElement(cv2.MORPH_RECT, (7,7))
    adth = cv2.adaptiveThreshold(hist, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 75, 11)
    adth = cv2.erode(adth, kernel, iterations = 1)

    #Canny()를 이용하여 외곽을 찾아 냄
    edged = cv2.Canny(adth, 10, 250)
    
    #컨투어를 찾는다. 
    contours, _ = cv2.findContours(edged.copy(),cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)
    total = 0
    
    # 외곽선 그리는 용도, 이미지에 그리기 때문에 이 코드 적용하면 원래 이미지에 초록색 선이 생김
    contours_image = cv2.drawContours(img, contours, -1, (0,255,0), 1)
    
    cv2.imwrite(str(path) + "wrinkle_" + user_name + "_" + img_name, contours_image)
    cv2.waitKey(0)

main(sys.argv[1], sys.argv[2])
