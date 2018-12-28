# MHW-Equipment-Optimizer

【魔物獵人：世界】裝備最佳化工具\
裝備列表版本與PC版同步

## 使用工具

-	Eclipse IDE for Java Developers (2018-09)
-	Java Development Kit (1.8.0_191)

## 如何使用

### 必要檔案：

安裝 Java Runtime Environment 1.8.0_191 或以上\
https://www.oracle.com/technetwork/java/javase/downloads/index.html \
確認系統路徑有JRE在裡面

下載最新版的程式壓縮檔(內含jar檔、裝備資訊檔案、範例)\
https://github.com/nh60211as/MHW-Equipment-Optimizer/releases

### 裝飾珠設定(設定完成後只要在獲得新的裝飾珠後修改即可)：

編輯/裝備檔案/_擁有裝飾珠.txt\
使用的換行符號是\n所以建議使用Notepad++來編輯檔案
```
#鑲嵌槽等級,等級
#技能名稱,需要裝飾品,擁有裝飾品
鑲嵌槽等級,3
耳塞,5,5
毒瓶追加,1,1
麻痺瓶追加,1,1
睡眠瓶追加,1,1
爆破瓶追加,1,1
匠,5,1
減輕膽怯,3,3
龍封力強化,1,1
屬性解放裝填擴充,3,0
...
```

### 裝備檔案設定：
/裝備檔案 目錄下的其他檔案盡量不要更動，若遊戲有新的裝備加入則有更新的必要

### 需求檔設定：

* 注意：文字檔所有以#開頭的行都會視為註解而不會實際讀取

以大劍.txt為例
```
#檔名：大劍.txt
#技能名稱,需要技能等級
系列需求：
龍騎士之證,4
需求：
#50%
弱點特效,3
#30%
看破,7
#5%
攻擊,4
超會心,3
無屬性強化,1
匠,2
挑戰者,2

排除：
屬性解放裝填擴充

武器：
龍熱機關式鋼翼改

```
"系列需求："下輸入要求的系列技能以及裝備個數，這裡是屍套龍的系列技能，若不需要系列技能請輸入"(無),0"\
"需求："下輸入要求的技能和等級數\
"排除："下輸入要排除的技能，若沒有要排除的技能則可以不填寫\
"武器："下輸入要求的武器名稱(目前我不打算輸入所有的武器，所以這部份請在/裝備檔案/_武器.txt自己加上)\
以下為可選輸入，如果有輸入的話就會強制選取那些裝備來配對，沒有的話會自動從已有裝備中搜尋\
"頭："、"身："、"腕："、"腰："、"腳："、"護石："同上，

### 輸入方式：
Windows 7、Windows 10 命令提示字元：
```
cd <MHW-Equipment-Optimizer.jar 的目錄>
java -jar MHW-Equipment-Optimizer.jar <需求檔案名稱.txt>
```

### 輸出範例：
```
java -jar MHW-Equipment-Optimizer.jar 狩獵笛.txt
符合條件的裝備：
龍熱機關式鋼翼改, 骷髏面罩α, 騰龍戰鎧α, 騰龍腕甲α, 騰龍腰甲α, 騰龍護腿α, 挑戰護石II
匠=2, 看破=7, 超會心=3, 攻擊=4, 強化持續=1, 挑戰者=2, 弱點特效=3, 無屬性強化=1
防禦力： 459, 屬性抗性：  -11,-9,+11,-9,+18, 剩餘鑲嵌槽： 0
龍熱機關式鋼翼改, 騰龍戰盔α, 鋼龍恐懼β, 騰龍腕甲α, 騰龍腰甲α, 騰龍護腿α, 挑戰護石II
超會心=3, 飛燕=1, 匠=2, 看破=7, 攻擊=4, 強化持續=1, 挑戰者=2, 弱點特效=3, 無屬性強化=1
防禦力： 453, 屬性抗性：  -8,-6,+9,-4,+14, 剩餘鑲嵌槽： 0
龍熱機關式鋼翼改, 騰龍戰盔α, 騰龍戰鎧α, 騰龍腕甲α, 火龍心腰甲β, 騰龍護腿α, 挑戰護石II
超會心=3, 飛燕=1, 看破=7, 攻擊=4, 匠=2, 挑戰者=2, 弱點特效=3, 無屬性強化=1
防禦力： 453, 屬性抗性：  -5,-8,+9,-8,+12, 剩餘鑲嵌槽： 0
龍熱機關式鋼翼改, 騰龍戰盔α, 騰龍戰鎧α, 騰龍腕甲α, 殘虐腰甲α, 騰龍護腿α, 挑戰護石II
超會心=3, 飛燕=1, 看破=7, 攻擊=4, 力量解放=1, 匠=2, 挑戰者=2, 弱點特效=3, 無屬性強化=1
防禦力： 453, 屬性抗性：  -7,-7,+9,-6,+13, 剩餘鑲嵌槽： 0
龍熱機關式鋼翼改, 騰龍戰盔α, 騰龍戰鎧α, 騰龍腕甲α, 蒼星之將腰帶α, 騰龍護腿α, 挑戰護石II
超會心=3, 飛燕=1, 看破=7, 攻擊=4, 匠=2, 挑戰者=2, 弱點特效=3, 無屬性強化=1
防禦力： 451, 屬性抗性：  -6,-6,+12,-8,+19, 剩餘鑲嵌槽： 0

```

## 未來實作功能：
* 顯示剩餘鑲嵌槽的等級
* 實作使用者介面
* 英語支援
