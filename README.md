# 功能介绍

包含两种识别方式，一种使用百度识别的 API，另一种使用 Tessaract 进行识别。

百度识别的 API 需要去官网申请自己的密匙，替换原来的密匙 [注册地址](https://cloud.baidu.com/product/ocr.html)；

Tessaract 需要配置环境，可以参考下面的笔记。

中文识别效果是百度接口更好用。

# 1. Tesseract 介绍

Tesseract的OCR引擎最先由HP实验室于1985年开始研发，至1995年时已经成为OCR业内最准确的三款识别引擎之一。然而，HP不久便决定放弃OCR业务，Tesseract也从此尘封。
 
数年以后，HP意识到，与其将Tesseract束之高阁，不如贡献给开源软件业，让其重焕新生－－2005年，Tesseract由美国内华达州信息技术研究所获得，并求诸于Google对Tesseract进行改进、消除Bug、优化工作。

Tesseract目前已作为开源项目发布在Google Project，其最新版本3.0已经支持中文OCR，并提供了一个命令行工具。

# 2. Tesseract 安装

Tesseract 可以在 linux、windows 和 macOS 下使用。这里以 macOS 为例。

### 2.1 使用 homebrew 安装 Tesseract 同时安装训练工具:

> brew install --with-training-tools tesseract

*虽然官方提供了多种语言的识别数据包，但是在很多情况下都需要在其基础上自己进行训练，所以记得安装训练工具*

### 2.2 安装语言识别包

从官网地址下载对应的识别包 [官网地址](https://github.com/tesseract-ocr/tessdata
) 比如中文识别选择 chi_sim 这个包

再将识别包放置到系统的相应目录中，比如我用 hoembrew 安装，地址位于：
> /usr/local/Cellar/tesseract/3.05.01/share/tessdata

### 2.3 下载训练工具

tesseract 的好处就在能够自己训练需要识别的字符，如果只是识别印刷字体这样的需求还是调用 API 比较方便，比如说百度云的文字识别 API。

要进行训练就下载 [jTessBoxEditor 样本训练工具](https://sourceforge.net/projects/vietocr/files/jTessBoxEditor/)

# 3. 基本概念

## 3.1 基本命令

查看版本
> tesseract -v

查看帮助
> tesseract --help

**进行识别的命令**
> tesseract in.jpg out.txt -l chi_sim -psm 6
> in.jpg:需要识别的图片
> out.jpg:图片的结果输出到的位置
> -l:后跟用于进行识别的数据包，这里是中文
> -psm:识别方式

## 3.2 语言识别包

查看已有的语言识别包

```
➜libai tesseract --list-langs
List of available languages (4):
chi_sim
eng
libai
number
```
语言识别的包是识别的基础，下载之后默认自带的是 *eng.traineddata* 英文识别包。他能够用来识别英文，但是不能识别中文。所以要下载中文的识别包 *ch_sim.traineddata* 这样就能识别中文。但是发现准确率不高，不符合需求就要训练自己的包。

总之核心就在 traineddata 文件。

### 3.3 psm的参数

psm 的参数很重要，表示 tesseract 识别图像的方式，比如说是一行一行识别还是逐字识别。希望逐字识别可以使用 -psm 10，希望逐行识别可以使用 -psm 6,其他没怎么用以后有机会补充。总之，**希望有更好的识别效果需要选择合适的 psm。**

```
tesseract --help-psm
  0    Orientation and script detection (OSD) only.
  1    Automatic page segmentation with OSD.
  2    Automatic page segmentation, but no OSD, or OCR.
  3    Fully automatic page segmentation, but no OSD. (Default)
  4    Assume a single column of text of variable sizes.
  5    Assume a single uniform block of vertically aligned text.
  6    Assume a single uniform block of text.
  7    Treat the image as a single text line.
  8    Treat the image as a single word.
  9    Treat the image as a single word in a circle.
 10    Treat the image as a single character.
```

谷歌翻译结果,自己尝试一下会有体会

```
方向和脚本检测（OSD）。
  1自动页面分割与OSD。
  2自动页面分割，但没有OSD或OCR。
  3全自动页面分割，但没有OSD。 （默认）
  4假设单列可变大小的文本。
  5假设一个垂直对齐的文本的统一块。
  6假设单个统一的文本块。
  7将图像视为单个文本行。
  8将图像视为单个字。
  9将图像视为一个单个的单词。
 10将图像视为单个字符。
```

# 4. 识别与训练

![libai](media/15060861840021/libai.jpg)

## 4.1 识别

> tesseract libai.jpg libai -l chi_sim -psm 6
```
抹甫曰月闫充，
屁是讹上霜;
亭孰塑明汛
佃爽崽故歹o
```
显然准确率不高，进行训练。

## 4.2 训练

1.生成字体文件

> 打开jTessBoxEditor工具，菜单栏：tools->Merge TIFF...，选中要合成的图片并保存为为：libai.tif 

2.生成 box 文件

> tesseract libai.tif libai -l chi_sim -psm 6 batch.nochop makebox

3.利用 jTessBoxEditor 校正

![说明](media/15060861840021/%E8%AF%B4%E6%98%8E.png)
校正之后记得保存。

4.生成.tr文件
> tesseract libai.tif libai -psm 6 nobatch box.train

5.生成unicharset文件
> unicharset_extractor libai.box

6.创建font_properties文件
> echo 'font 0 0 0 0 0' > font_properties

7.training
> shapeclustering -F font_properties -U unicharset libai.tr
> mftraining -F font_properties -U unicharset -O l libai.tr

8.Clustering。产生字符形状正常化特征文件normproto
> cntraining libai.tr

9.重命名文件

```
mv normproto libai.normproto
mv inttemp libai.inttemp
mv pffmtable libai.pffmtable
mv shapetable libai.shapetable
mv unicharset libai.unicharset
```
10.合并文件生成 traineddata 文件

```
➜  libai combine_tessdata libai.
Combining tessdata files
TessdataManager combined tesseract data files.
Offset for type  0 (libai.config                ) is -1
Offset for type  1 (libai.unicharset            ) is 140
Offset for type  2 (libai.unicharambigs         ) is -1
Offset for type  3 (libai.inttemp               ) is 855
Offset for type  4 (libai.pffmtable             ) is 140145
Offset for type  5 (libai.normproto             ) is 140274
Offset for type  6 (libai.punc-dawg             ) is -1
Offset for type  7 (libai.word-dawg             ) is -1
Offset for type  8 (libai.number-dawg           ) is -1
Offset for type  9 (libai.freq-dawg             ) is -1
Offset for type 10 (libai.fixed-length-dawgs    ) is -1
Offset for type 11 (libai.cube-unicharset       ) is -1
Offset for type 12 (libai.cube-word-dawg        ) is -1
Offset for type 13 (libai.shapetable            ) is 141781
Offset for type 14 (libai.bigram-dawg           ) is -1
Offset for type 15 (libai.unambig-dawg          ) is -1
Offset for type 16 (libai.params-model          ) is -1
Output libai.traineddata created successfully.
```

11.将文件拷贝到目标文件夹

> cp libai.traineddata /usr/local/Cellar/tesseract/3.05.01/share/tessdata

12.再次识别

> tesseract libai.jpg trained -l libai -psm 6

```
➜  libai cat trained.txt
床前明月光，
疑是地上霜;
举头望明月，
低头思故乡.
```
# 6. 只识别数字

```
tesseract 172.jpg whitelist/172 -c tessedit_char_whitelist=0123456789

tesseract 183.jpg digits/183 digits
```

> 参考资料：
> 
> [Tesseract 3 语言数据的训练方法](https://www.bbsmax.com/A/qVdeBVyEzP/)
> [使用Tesseract-OCR训练文字识别记录](http://www.suohi.cc/posts/5810974b731d7c006f9daf77)
> [如何使用Tesseract-OCR(v3.02.02)训练字库 - 作业部落 Cmd Markdown 编辑阅读器](https://www.zybuluo.com/xxzhushou/note/807167)
> 

