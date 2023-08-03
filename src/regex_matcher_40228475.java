import java.io.*;
import java.util.*;

 public class regex_matcher_40228475 {


    static List<String>  getMatchingStrings(List<String> words,String regex){
        List<String> matchedStrings=new ArrayList<>();

        words.sort(String::compareTo);

        for(String word: words){
            if(word.toLowerCase().matches(regex.toLowerCase())){
                matchedStrings.add(word);
            }
        }
         return matchedStrings;
    }


    public static void writeToFile( String lcs) throws IOException {
        String outputFile =System.getProperty("user.dir")+"\\src\\output.txt";
        BufferedWriter out = new BufferedWriter(new FileWriter(outputFile));
        out.write(lcs);
        out.newLine();
        out.close();
    }

    public static String getLCSof2(int[][] dp,int i,int j,String a,String b){
        if(i==0|| j==0)return "";

//        if(dp[i][j]==(dp[i-1][j-1]+1)){
        if(a.charAt(i-1)==b.charAt(j-1)){
            return getLCSof2(dp,i-1,j-1,a,b)+a.charAt(i-1);
        }
        if(dp[i][j-1]>dp[i-1][j]){
            return getLCSof2(dp,i,j-1,a,b);
        }
        return getLCSof2(dp,i-1,j,a,b);
    }

    public static String getLCSof3(int[][][] dp,int i,int j,int k,String a,String b,String c){
        if(i==0|| j==0 || k==0)return "";

        if(a.charAt(i-1)==b.charAt(j-1)&&a.charAt(i-1)==c.charAt(k-1)){
            return getLCSof3(dp,i-1,j-1,k-1,a,b,c)+a.charAt(i-1);
        }
        if(dp[i-1][j][k]>=dp[i][j-1][k] && dp[i-1][j][k]>=dp[i][j][k-1]){
            return getLCSof3(dp,i-1,j,k,a,b,c);
        }
        if(dp[i][j-1][k]>=dp[i-1][j][k] && dp[i][j-1][k]>=dp[i][j][k-1]){
            return getLCSof3(dp,i,j-1,k,a,b,c);
        }
        return getLCSof3(dp,i,j,k-1,a,b,c);
    }
    public static String LCSof2(String a,String b){
        int n=a.length();
        int m=b.length();
        int dp[][]=new int[n+1][m+1];

        for(int i=0;i<=n;i++){
            dp[i][0]=0;
        }
        for(int i=0;i<=m;i++){
            dp[0][i]=0;
        }

        for(int i=1;i<=n;i++){
            for (int j=1;j<=m;j++){
                if(a.charAt(i-1)==b.charAt(j-1)){
                    dp[i][j]=1+dp[i-1][j-1];
                }
                else{
                    dp[i][j]=Math.max(dp[i-1][j],dp[i][j-1]);
                }
            }
        }

//        for(int i=0;i<=n;i++){
//            for(int j=0;j<=m;j++){
//                System.out.print(dp[i][j]+" ");
//            }
//            System.out.println();
//        }

        return getLCSof2(dp,n,m,a,b);
    }

    public static String LCSof3(String a,String b,String c){

        int n=a.length();
        int m=b.length();
        int o=c.length();
        int dp[][][]=new int[n+1][m+1][o+1];

        for(int i=0;i<=n;i++){
            dp[i][0][0]=0;
        }
        for(int i=0;i<=m;i++){
            dp[0][i][0]=0;
        }
        for(int i=0;i<=o;i++){
            dp[0][0][i]=0;
        }

        for(int i=1;i<=n;i++){
            for (int j=1;j<=m;j++){
                for(int k=1;k<=o;k++){
                    if(a.charAt(i-1)==b.charAt(j-1) && b.charAt(j-1)==c.charAt(k-1)){
                        dp[i][j][k]=1+dp[i-1][j-1][k-1];
                    }
                    else{
                        dp[i][j][k]=Math.max(Math.max(dp[i-1][j][k],dp[i][j-1][k]),dp[i][j][k-1]);
                    }
                }

            }
        }



        return getLCSof3(dp,n,m,o,a,b,c);
    }


    public static String findLongestCommonSubsequence(List<String> matchedWords){
        switch (matchedWords.size()){
            case 0:
                return "No Common Sub Sequence";

            case 1:
                return matchedWords.get(0);
            case 2:
                return LCSof2(matchedWords.get(0),matchedWords.get(1));
            case 3:
            default:
                return LCSof3(matchedWords.get(0),matchedWords.get(1),matchedWords.get(2));





        }
    }


    public static void main(String[] args) throws IOException {
        String inputURL = System.getProperty("user.dir")+"\\src\\input.txt";
        BufferedReader br=new BufferedReader(new FileReader(inputURL));
        int n= Integer.parseInt(br.readLine().trim());
        List<String> words=new ArrayList<>(n);
        for(int i=0;i<n;i++){
            words.add(br.readLine().trim());
        }

        String regex=br.readLine().trim();
        br.close();

        List<String> matchedWords=getMatchingStrings(words,regex);
//        System.out.println(matchedWords);
//        System.out.println(regex+" "+words+" "+n);
        String lcs=findLongestCommonSubsequence(matchedWords);
//        System.out.println(lcs);
        writeToFile(lcs);
    }
}