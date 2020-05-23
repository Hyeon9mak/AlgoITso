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
    #imge resize -> 512x 512
    img = Image.open('ttest2.png')
    img = img.resize((512, 512))
    img.save('resized.png')
    img = cv2.imread('resized.png')
    cv2.imshow("origin", img)
    
    
    # B-Space 추출 -> 실제 피부색을 띄고 있기에 red, green으로는 구분 불가
    B, G, R = cv2.split(img)  
    cv2.imshow("B-Space", B)
    
    # Bilateral Filter 적용 -> 잡음 제거, intensity값과 위치정보 값을 모두 사용
    blur = cv2.bilateralFilter(B, 15, 75, 75)  # 인자 값 (원본 이미지, 커널 크기, 색공간 표준편차, 거리공간 표준편차) 색과 거리가 멀어져도 픽셀간에 서로 영향을 받음 )
    cv2.imshow("BilateralFiler", blur)
    
    # Histogram equlization -> 명암 대비 영상
    hist = cv2.equalizeHist(blur)   
    cv2.imshow("Histogram Equalization", hist)
    
    #Adaptive Threshold -> 확실한 주름 + 주름으로 의심되는 부분
    # adaptiveThreshold(원본 이미지, 조건값에 대한 픽셀값의 최대값, 주위 픽셀값 처리, 결과값 처리 방식, 
    #                   주위값의 픽셀을 얻을 window의 크기, C(주위 픽셀값 처리하고 마지막 C를 더함)의 값) )
    adth = cv2.adaptiveThreshold(hist, 255, cv2.ADAPTIVE_THRESH_MEAN_C, cv2.THRESH_BINARY_INV, 71, 0)
    cv2.imshow("Adatpive Threshold", adth)
    
    """
    명암 대비 영상(hist)에 Hough line transform 적용 -> 선형 검출 -> 여집합 선형이 없는 영역 추출
    Threshold의 주름으로 의심되는 영역은 주름 이외에도 색상이 진한 반점 등이 포함될 수 있기에 주름이 아닌
    영역을 제거하기 위하여, 주름 의심 영역에서 선형이 없는 영역을 제거한다.
    """
    
    # Canny를 통한 edge 검출 | 인자 값 (원본 이미지, minimum thresholding value, maximum threshilding value,
    #                            apertureSize(커널 크기), L2gradient(true=선공식, flase=절대값선근사값공식))
    hist = cv2.equalizeHist(blur)
    edges = cv2.Canny(hist,50, 150)
    cv2.imshow("edges imge", edges)
    
    minLineLength = 30                  # 선의 최소 길이, 이보다 짧은 선분은 버려짐
    maxLineGap = 10                     # 단일 선으로 처리하기 위해 선분 간에 허용되는 최대 간격
    
    # 인자 값(원본 이미지, p 정확도,θ 정확도, 임계값(선으로 판단되기 위해 얻어야 하는 최소한의 표), minLineLength, maxLineGap)
    lines = cv2.HoughLinesP(edges, 1, np.pi/180, 100, minLineLength, maxLineGap)
    
    for line in lines:
        x1, y1, x2, y2 = line[0]
        cv2.line(img, (x1,y1), (x2,y2), (0,255,0), 2)
        
    cv2.imshow("Hough line Transform",img)          #선형 검출 이걸 수정하긴 해야함
    cv2.waitKey(0)
    
main()