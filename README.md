# nlpir-analysis-cn-ictclas-6.6.0
The java package of nlpir/ictclas for lucene/solr 6.6.0 plugin.

Support: MacOS,Linux x86/64, Windows x86/64

* DEMO
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
* Lucene DEMO
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
* Solr

The plugin file ,nlpir.properties contains: 1.data 2.encoding 3.license code 4.user dictory 5.override the user dic.
Put the file to solr_home/server/, and the data need to set the path of NLPIR/ICTCLAS Data.
![Alt text](https://github.com/NLPIR-team/nlpir-analysis-cn-ictclas/blob/master/solr.png)
The plugin jar put the classpath.
