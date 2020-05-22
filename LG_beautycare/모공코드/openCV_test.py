import cv2

def algoITso_MedianBlur(l) :
    l_rows, l_cols = l.shape
    q = []
    for r in range(l_rows) :
        for c in range(l_cols) :
            q.append(l[r][c])
    
    q.sort()
    return q[len(q)//2]

def algoITso_MedianBlur_Setup(src, KsizeX, KsizeY) :
    dst = src
    src_rows, src_cols = src.shape
    Xr = KsizeX//2
    Yr = KsizeY//2

    for r in range(Yr, src_rows - Yr) :
        for c in range(Xr, src_cols - Xr) :
            window = src[r-Yr:(r-Yr)+KsizeY, c-Xr:(c-Xr)+KsizeX]
            dst[r,c] = algoITso_MedianBlur(window)

    return dst


############### code at 2020-03-XX-2
# def algoITso_medianBlur(source_img, kernel_X, kernel_Y) :
#     median_img = source_img
#     result_img = median_img
#     rows, cols = source_img.shape
#     kernel_Xr = kernel_X//2
#     kernel_Yr = kernel_Y//2

#     for r in range(kernel_Yr, rows-kernel_Yr) :
#         for c in range(kernel_Xr, cols-kernel_Xr) :
#             median_img = source_img[r-kernel_Yr:kernel_Y, c-kernel_Xr:kernel_X]
#             median_rows, median_cols = median_img.shape
#             median_list = []
        
#             for i in range(median_rows) :
#                 for j in range(median_cols) :
#                     print(median_img[i][j])
#                     median_list.append(median_img[i][j])

#             median_list.sort()
#             result_img[r][c] = median_list[len(median_list)//2]

############## code at 2020-03-XX-1
    # for i in range(img_height-3) :
    #     for j in range(img_weight-7) : 
    #         mask = []
    #         for mask_i in range(i+3) :
    #             for mask_j in range(j+7) :
    #                 mask.append(blur_img[mask_i][mask_j])
    #         mask.sort()
    #         result_img[i+1][j+3] = mask[len(mask)//2]

    return dst

def main() :
    original_img = cv2.imread("/Users/hyeon9mak/Desktop/img_00.png", cv2.IMREAD_COLOR)
    gray_img = cv2.cvtColor(original_img, cv2.COLOR_BGR2GRAY)
    
    median_img = algoITso_MedianBlur_Setup(gray_img, 7, 3)
    median_img = algoITso_MedianBlur_Setup(median_img, 3, 7)

    #cv2.imshow("Original", original_img)
    #cv2.imshow("Gray Scale", gray_img)
    cv2.imshow("Median Blur", median_img)
    cv2.waitKey(0)
    cv2.destroyAllWindows()
    return

main()
