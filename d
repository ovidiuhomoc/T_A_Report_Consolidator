[1mdiff --git a/Resources/Stocare_Cod_Temp.txt b/Resources/Stocare_Cod_Temp.txt[m
[1mdeleted file mode 100644[m
[1mindex d154320..0000000[m
[1m--- a/Resources/Stocare_Cod_Temp.txt[m
[1m+++ /dev/null[m
[36m@@ -1,62 +0,0 @@[m
[31m-private boolean quotedStart = false;[m
[31m-	private boolean addWord = false;[m
[31m-	private boolean addChar = false;[m
[31m-	private boolean skipNext = false;[m
[31m-[m
[31m-	private String condChecker(String temp, char charX, int currPos, int length) {[m
[31m-[m
[31m-		if (this.skipNext == true) {[m
[31m-			this.skipNext = false;[m
[31m-			return temp;[m
[31m-		}[m
[31m-		[m
[31m-		if (this.isLastQuote())) {[m
[31m-			this.addWord=true;[m
[31m-			this.quotedStart=false;[m
[31m-		}[m
[31m-		[m
[31m-		if (this.consecutiveQuote()){[m
[31m-			this.addChar = true;[m
[31m-			this.skipNext = true;[m
[31m-		}[m
[31m-		[m
[31m-		if(this.quoteEnd()) {[m
[31m-			this.quotedStart=false;[m
[31m-			return temp;[m
[31m-		}[m
[31m-		[m
[31m-		if (this.quoteStart()) {[m
[31m-			this.quotedStart=true;[m
[31m-			return temp;[m
[31m-		}[m
[31m-		[m
[31m-		if (this.isDelimAdded()) {[m
[31m-			this.addChar = true;[m
[31m-		}[m
[31m-		[m
[31m-		if (this.isEntryEnd()) {[m
[31m-			this.addWord=true;[m
[31m-		}[m
[31m-		[m
[31m-		if (this.isLastChar()) {[m
[31m-			this.addChar=true;[m
[31m-			this.addWord=true;[m
[31m-		}[m
[31m-		[m
[31m-		if (ok()) {[m
[31m-			this.addChar=true;[m
[31m-		}[m
[31m-[m
[31m-		// de inlaturat functia de mai sus si inversat logica; by default adaug caracterul si la exceptii sar[m
[31m-		// add word functie separata si aici doar ridic steag care indica necesitatea [m
[31m-		[m
[31m-		if (this.addChar == true) {[m
[31m-			this.addChar = false;[m
[31m-			temp = temp + charX;[m
[31m-		}[m
[31m-[m
[31m-		if (this.addWord == true) {[m
[31m-			this.addWord = false;[m
[31m-			this.results.add(temp);[m
[31m-		}[m
[31m-		return temp;[m
