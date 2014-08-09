package analyze_tagged_sentences;

import read_tagged_sentences.*;

/*Testing if these changes are visible*/
public class ReviewEntityAndAttributes {

	Tagged t;
	TaggedEntity e;
	TaggedAttribute a;

	public ReviewEntityAndAttributes() {
		
		
		System.out.println("\nRemoved attributes :");
		for (int i = 0; i < ReadTaggedTestDataXml.TaggedSequence.size(); i++) {
			t = ReadTaggedTestDataXml.TaggedSequence.get(i);

			for (int k = 0; k < t.Entities.size(); k++) {
				e = t.Entities.get(k);

				for (int m = 0; m < e.Attr.size();) {
					a = e.Attr.get(m);

					if (ReadTaggedTestDataXml.EntityNames
							.contains(a.attribute_name)) {
						System.out.println(
								a.attribute_name);

						e.Attr.remove(m);

					} else
						m++;
				}

			}
		}
	}
}
