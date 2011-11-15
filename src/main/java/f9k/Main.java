package f9k;


import f9k.ops.MemoryElement;
import f9k.ops.OPS;
import f9k.ops.QueryElement;
import f9k.ops.QueryPair;
import f9k.ops.Rule;
import f9k.ops.commands.Command;
import f9k.ops.commands.ProductionSpec;
import f9k.ops.commands.nlg;
import f9k.ops.commands.nlg_agg;
import f9k.ops.commands.remove;
import f9k.ops.commands.write;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import simplenlg.features.Tense;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.lexicon.NIHDBLexicon;
import simplenlg.realiser.english.Realiser;


public class Main
{
  static String DB_FILENAME = "/lexicon/lexAccess2011lite/data/HSqlDb/lexAccess2011.data";

  static Command _nlg;
  static Command _nlgAgg;
  static Command _remove;

  static Lexicon _lexicon;
  static NLGFactory _nlgFactory;
  static Realiser _realiser;

  public static String getPwd()
  {
    File file = new File(".");
    try
    {
      return file.getCanonicalPath();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    return "";
  }

  public static void main(String[] args)
      throws Exception
  {
    _lexicon = new NIHDBLexicon(getPwd() + DB_FILENAME);
    _nlgFactory = new NLGFactory(_lexicon);
    _realiser = new Realiser(_lexicon);

    _nlg = new nlg(_nlgFactory, _realiser, _lexicon);
    _nlgAgg = new nlg_agg(_nlgFactory, _realiser, _lexicon);
    _remove = new remove();

    if (args.length > 0)
    {
      File file = new File(args[0]);
      if (!file.exists())
      {
        System.out.println("file not found: " + args[0]);
      }
      processOPSFile(file);
    }
    else
    {
      runSample();
    }
  }

  private static OPS processOPSFile(File file)
      throws Exception
  {
    OPS ops = new OPS();

    JSONObject obj = readJSONFile(file.getCanonicalPath());
    if (obj.has("name"))
    {
      System.out.println("processing ops: " + obj.getString("name"));
    }
    if (!obj.has("ops"))
    {
      throw new IllegalArgumentException("missing ops section");
    }
    JSONArray arr = obj.getJSONArray("ops");
    for (int i = 0; i < arr.length(); i++)
    {
      JSONArray statement = arr.getJSONArray(i);
      if (statement.length() < 2)
      {
        System.out.println("malformed statement: " + statement);
      }

      String cmd = statement.getString(0);
      if (cmd.equals("literalize"))
      {
        String recordName = statement.getString(1);

        Map<String, Object> values = new HashMap<String, Object>();
        for (int j = 2; j < statement.length(); j++)
        {
          Object field = statement.get(j);
          if (field instanceof String)
          {
            values.put(field.toString(), null);
          }
          else if (field instanceof JSONObject)
          {
            JSONObject fieldObj = (JSONObject)field;
            Iterator<String> keys = fieldObj.keys();
            while (keys.hasNext())
            {
              String key = keys.next();
              values.put(key, fieldObj.get(key));
            }
          }
        }

        ops.literalize(new MemoryElement(recordName, values));
      }
      else if (cmd.equals("make"))
      {
        String recordName = statement.getString(1);

        Map<String, Object> values = new HashMap<String, Object>();
        for (int j = 2; j < statement.length(); j++)
        {
          Object field = statement.get(j);
          if (field instanceof String)
          {
            values.put(field.toString(), null);
          }
          else if (field instanceof JSONObject)
          {
            JSONObject fieldObj = (JSONObject)field;
            Iterator<String> keys = fieldObj.keys();
            while (keys.hasNext())
            {
              String key = keys.next();
              values.put(key, fieldObj.get(key));
            }
          }
        }

        ops.make(new MemoryElement(recordName, values));
      }
      else if (cmd.equals("p"))
      {
        String productionName = statement.getString(1);

        List<QueryElement> query = new ArrayList<QueryElement>();
        JSONArray list = statement.getJSONArray(2);
        for (int j = 0; j < list.length(); j++)
        {
          JSONArray matcher = list.getJSONArray(j);
          String recordName = matcher.getString(0);
          Object[] values = parseValues(1, statement);
          query.add(new QueryElement(recordName, values));
        }
        query.add(new QueryElement("goal", "type", "generate"));
        query.add(new QueryElement("sphrase", "actor", "$actor", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

        list = statement.getJSONArray(3);

        Map<String, Object> values = parseKeyValues(2, statement);

        List<ProductionSpec> productions = new ArrayList<ProductionSpec>();


      }
    }

    return ops;
  }

  private static Object[] parseValues(int startIdx, JSONArray arr)
      throws JSONException
  {
    Object[] objects = new Object[arr.length() - startIdx];
    for (int i = startIdx, j = 0; i < arr.length(); i++, j++)
    {
      objects[j] = arr.get(i);
    }
    return objects;
  }

  private static Map<String, Object> parseQueryPairs(int startIdx, JSONArray arr)
      throws JSONException
  {
    List<QueryPair> values = new ArrayList<QueryPair>();
    for (int j = startIdx; j < arr.length(); j++)
    {
      Object field = arr.get(j);
      JSONObject fieldObj = (JSONObject)field;
      Iterator<String> keys = fieldObj.keys();
      while (keys.hasNext())
      {
        String key = keys.next();
        values.put(key, fieldObj.get(key));
      }
    }
    return values;
  }

  private static Map<String, Object> parseKeyValues(int startIdx, JSONArray arr)
      throws JSONException
  {
    Map<String, Object> values = new HashMap<String, Object>();
    for (int j = startIdx; j < arr.length(); j++)
    {
      Object field = arr.get(j);
      if (field instanceof String)
      {
        values.put(field.toString(), null);
      }
      else if (field instanceof JSONObject)
      {
        JSONObject fieldObj = (JSONObject)field;
        Iterator<String> keys = fieldObj.keys();
        while (keys.hasNext())
        {
          String key = keys.next();
          values.put(key, fieldObj.get(key));
        }
      }
    }
    return values;
  }

  public static JSONObject readJSONFile(String path)
      throws IOException, JSONException
  {
    return new JSONObject(readFile(path));
  }

  // http://stackoverflow.com/questions/326390/how-to-create-a-java-string-from-the-contents-of-a-file
  public static String readFile(String path)
      throws IOException
  {
    FileInputStream stream = new FileInputStream(new File(path));
    try
    {
      FileChannel fc = stream.getChannel();
      MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
      /* Instead of using default, pass in a decoder. */
      return Charset.defaultCharset().decode(bb).toString();
    }
    finally
    {
      stream.close();
    }
  }

  private static void runSample()
  {
    OPS ops = new OPS();

    ops.literalize(new MemoryElement("sphrase", "actor", null, "verb", null, "verb.tense", Tense.PRESENT, "object", null));
    ops.literalize(new MemoryElement("goal", "type", null));

    ops.make(new MemoryElement("goal", "type", "generate"));

    ops.make(new MemoryElement("sphrase", "actor", "Brian", "verb", "share", "verb.tense", Tense.PAST, "object", "article"));
    ops.make(new MemoryElement("sphrase", "actor", "Joe", "verb", "comment on", "verb.tense", Tense.PAST, "object", "post"));

    ops.make(new MemoryElement("sphrase", "actor", "John", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));
    ops.make(new MemoryElement("sphrase", "actor", "Larry", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));
    ops.make(new MemoryElement("sphrase", "actor", "Sally", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Joe"));

//    ops.make(new MemoryElement("sphrase", "actor", "Robert", "verb", "connect to", "verb.tense", Tense.PRESENT, "object", "Joe"));

//    ops.make(new MemoryElement("sphrase", "actor", "Robert", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Mary"));
//    ops.make(new MemoryElement("sphrase", "actor", "Brian", "verb", "connect to", "verb.tense", Tense.PAST, "object", "Mary"));

    ops.addRule(createGenerateRule());
    ops.addRule(createAggregateCommonVerbObjectRule());
    ops.addRule(createGenerateStopRule());

    ops.run();
  }

  private static Rule createGenerateRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(new write("actor: {0} verb: {1} object: {2}"), new Object[] { "$actor", "$verb", "$object" }));
    productions.add(new ProductionSpec(_nlg, new Object[] { "$actor", "$verb", "$verb.tense", "$object" }));
    productions.add(new ProductionSpec(_remove, new Object[] { 1 }));

    return new Rule("generate", query, productions);
  }

  private static Rule createAggregateCommonVerbObjectRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));
    query.add(new QueryElement("sphrase", "actor", "$actor1", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));
    query.add(new QueryElement("sphrase", "actor", "$actor2", "verb", "$verb", "verb.tense", "$verb.tense", "object", "$object"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(_nlgAgg, new Object[] { "$actor1", "$actor2", "$verb", "$verb.tense", "$object" }));
    productions.add(new ProductionSpec(_remove, new Object[] { 1 }));
    productions.add(new ProductionSpec(_remove, new Object[] { 2 }));

    return new Rule("generate", query, productions);
  }

  private static Rule createGenerateStopRule()
  {
    List<QueryElement> query = new ArrayList<QueryElement>();
    query.add(new QueryElement("goal", "type", "generate"));

    List<ProductionSpec> productions = new ArrayList<ProductionSpec>();
    productions.add(new ProductionSpec(_remove, new Object[] { 0 }));

    return new Rule("generate_stop", query, productions);
  }
}
