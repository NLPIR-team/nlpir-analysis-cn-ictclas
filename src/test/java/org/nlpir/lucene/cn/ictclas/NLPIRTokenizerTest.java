package org.nlpir.lucene.cn.ictclas;

import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

public class NLPIRTokenizerTest {

	public static void main(String[] args) throws Exception {
		// NLPIR
		NLPIRTokenizerAnalyzer nta = new NLPIRTokenizerAnalyzer("", 1, "", "", false);
		// Index
		IndexWriterConfig inconf = new IndexWriterConfig(nta);
		inconf.setOpenMode(OpenMode.CREATE_OR_APPEND);
		IndexWriter index = new IndexWriter(FSDirectory.open(Paths.get("index/")), inconf);
		Document doc = new Document();
		doc.add(new TextField("contents",
				"特朗普表示，很高兴汉堡会晤后再次同习近平主席通话。我同习主席就重大问题保持沟通和协调、两国加强各层级和各领域交往十分重要。当前，美中关系发展态势良好，我相信可以发展得更好。我期待着对中国进行国事访问。",
				Field.Store.YES));
		index.addDocument(doc);
		index.flush();
		index.close();
		// Search
		String field = "contents";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("index/")));
		IndexSearcher searcher = new IndexSearcher(reader);
		QueryParser parser = new QueryParser(field, nta);
		Query query = parser.parse("特朗普习近平");
		TopDocs top = searcher.search(query, 100);
		System.out.println("总条数：" + top.totalHits);
		ScoreDoc[] hits = top.scoreDocs;
		for (int i = 0; i < hits.length; i++) {
			System.out.println("doc=" + hits[i].doc + " score=" + hits[i].score);
			Document d = searcher.doc(hits[i].doc);
			System.out.println(d.get("contents"));
		}

	}
}
