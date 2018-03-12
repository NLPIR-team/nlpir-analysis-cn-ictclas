# Lucene-analyzers-nlpir-ictclas-6.6.0 V2.0

NLPIR/ICTCLAS for Lucene/Solr 6.6.0 analyzer plugin. Support: MacOS,Linux x86/64, Windows x86/64

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

1. the plugin jar, which you have built and put it in your core's lib directory.
2. nlpir.properties contains: 

```
data="" #Data directory‘s parent path
encoding=1 #0 GBK;1 UTF-8
sLicenseCode="" # License code
userDict="" # user dictionary, a text file
bOverwrite=false # whether overwrite the existed user dictionary or not
```

3. data directory, you can find it in NLPIR SDK <https://github.com/NLPIR-team/NLPIR/tree/master/NLPIR%20SDK/NLPIR-ICTCLAS>

Waring: You need to make sure the plugin jar can find the nlpir.properties file. You can put the file to solr_home/server/, and the data need to set the path of NLPIR/ICTCLAS Data.

* Solr Managed-schema

```
  <fieldType name="text_general" class="solr.TextField" positionIncrementGap="100">
    <analyzer type="index">
      <tokenizer class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizerFactory"/>
    </analyzer>
    <analyzer type="query">
      <tokenizer class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizerFactory"/>
    </analyzer>
  </fieldType>
```

4. dependency jar for dll: jna.jar. add to your solr's lib.

# Tokenizer

* v1.*

```
//Standard Tokenizer
class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizerFactory"
```

* v2.*

```
//Standard Tokenizer
class="org.nlpir.lucene.cn.ictclas.NLPIRTokenizerFactory"
//Finer Segment
class="org.nlpir.lucene.cn.ictclas.finersegmet.FinerTokenizerFactory"
```

# Solr Show

![Alt text](https://github.com/NLPIR-team/nlpir-analysis-cn-ictclas/blob/master/solr.png)
