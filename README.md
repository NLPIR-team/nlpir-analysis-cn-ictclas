# Now NLPIR/ICTCLAS for Lucene/Solr plugin V2.2 

# Lucene-analyzers-nlpir-ictclas-8.4.1

NLPIR/ICTCLAS for Lucene/Solr 8.4.1 analyzer plugin. Support: MacOS,Linux x86/64, Windows x86/64

The project resources folder is a source folder, which contains all platform's dynamic libraries and push them to the classpath.//Source Folder 保证所有平台下的动态库自动部署到classpath环境下，以便JNA加载动态库。

# Building Lucene-analyzers-nlpir-ictclas

Lucene-analyzers-nlpir-ictclas is built by Maven. To build Lucene-analyzers-nlpir-ictclas run:

```bash
mvn clean package -DskipTests
```
Or if you use IDE(Eclipse), there is also the same way.
# How to use in your projects

You can use NLPIRTokenizerAnalyzer to do the Chinese Word Segmentation:

* NLPIRTokenizerAnalyzer DEMO

```java
        String text="我是中国人";
        NLPIRTokenizerAnalyzer nta = new NLPIRTokenizerAnalyzer("", 1, "", "", false);
        TokenStream  ts  = nta.tokenStream("word", text);  
        ts.reset();
        CharTermAttribute  term = ts.getAttribute(CharTermAttribute.class);
        while(ts.incrementToken()){
            System.out.println(term.toString());
        }
        ts.end();
        ts.close();
        nta.close();
```
and also use in Lucene：

* Lucene DEMO

The sample shows how to index your text and search by using NLPIRTokenizerAnalyzer.

```java
        //For indexing
        NLPIRTokenizerAnalyzer nta = new NLPIRTokenizerAnalyzer("", 1, "", "", false);
        IndexWriterConfig inconf=new IndexWriterConfig(nta);
        inconf.setOpenMode(OpenMode.CREATE_OR_APPEND);
        IndexWriter index=new IndexWriter(FSDirectory.open(Paths.get("index/")),inconf);
        Document doc = new Document();
        doc.add(new TextField("contents", "特朗普表示，很高兴汉堡会晤后再次同习近平主席通话。我同习主席就重大问题保持沟通和协调、两国加强各层级和各领域交往十分重要。当前，美中关系发展态势良好，我相信可以发展得更好。我期待着对中国进行国事访问。",Field.Store.YES));
        index.addDocument(doc);
        index.flush();
        index.close();
        //for searching
        String field = "contents";
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index/")));
        IndexSearcher searcher = new IndexSearcher(reader);
        QueryParser parser = new QueryParser(field, nta);
        Query query = parser.parse("特朗普习近平");
        TopDocs top=searcher.search(query, 100);
        ScoreDoc[] hits = top.scoreDocs;
        for(int i=0;i<hits.length;i++) {
          System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
          Document d = searcher.doc(hits[i].doc);
          System.out.println(d.get("contents"));
        }
```

# How Solr Install

To make part of Solr, you need these files: 

0. git clone the src(git clone https://github.com/NLPIR-team/nlpir-analysis-cn-ictclas.git) 
and Data directory which u can find in NLPIR SDK <https://github.com/NLPIR-team/NLPIR/tree/master/NLPIR%20SDK/NLPIR-ICTCLAS>
u should configure the configuration items in resources/nlpir.properties in advance,and then build the jar with the property file
(This step can be skipped,however, if you have problems with your configuration later, follow the instructions in this step)


1. the plugin jar which you have built or download from this site ,and put it in your core's lib directory(solr-8.4.1/server/solr-webapp/webapp/WEB-INF/lib/).


2. nlpir.properties contains: 

```
data=/solr-8.4.1/server/ #Data directory‘s parent path
encoding=1 #0 GBK;1 UTF-8
sLicenseCode=  #none
userDict=  # user dictionary, a text file(userDict.txt)
bOverwrite=false # whether overwrite the existed user dictionary or not
```

3. data directory, u can find it in NLPIR SDK <https://github.com/NLPIR-team/NLPIR/tree/master/NLPIR%20SDK/NLPIR-ICTCLAS>
the data directory should be positioned under /solr-8.4.1/server/
And u need to ensure that the authorization file (.user) in the data folder is valid
u can find it in <https://github.com/NLPIR-team/NLPIR/tree/master/License/license%20for%20a%20month>


Waring: You need to make sure the plugin jar can find the nlpir.properties file. You can put the file to solr_home/server/, and the data need to set the path of Data.

* Solr Managed-schema

```
  <fieldType name="text_nlpir" class="solr.TextField">
    <analyzer type="index">
      <tokenizer class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizerAnalyzer"/>
    </analyzer>
    <analyzer type="query">
      <tokenizer class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizerAnalyzer"/>
    </analyzer>
  </fieldType>
```

4. dependency jar for dll: jna.jar(4.1.0). add to your solr's lib(solr-8.4.1/server/solr-webapp/webapp/WEB-INF/lib/).

# Tokenizer

* v2.*

```
//Standard Tokenizer
class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizer"
//Finer Segment
class="org.nlpir.lucene.cn.ictclas.finersegmet.FinerTokenizer"
```

* v1.*

```
//Standard Tokenizer
class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizer"
```

# Solr Show

![Alt text](https://github.com/NLPIR-team/nlpir-analysis-cn-ictclas/blob/master/solr.png)
