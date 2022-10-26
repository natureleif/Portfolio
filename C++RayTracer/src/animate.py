import cv2
from PIL import Image
import os

folder = r"C:\Users\akuhl\OneDrive\Documents\GitHub\CS-360\HW5\out"
img_array = []
for i in range(500):
    file = str(i)+".ppm"
    img = cv2.imread(os.path.join(folder,file))
    #cv2.imwrite(str(i)+".jpg",i)
    height, width, layers = img.shape
    size = (width,height)
    img_array.append(img)

out = cv2.VideoWriter('project.mp4',cv2.VideoWriter_fourcc(*'mp4v'), 15, size)
 
for i in range(len(img_array)):
    out.write(img_array[i])
for i in range(len(img_array)):
    j = i + 1
    out.write(img_array[-j])
out.release()