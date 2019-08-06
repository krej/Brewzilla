package beer.unaccpetable.brewzilla;

import com.unacceptable.unacceptablelibrary.Tools.Tools;

import org.junit.Assert;
import org.junit.Test;

import beer.unaccpetable.brewzilla.Models.BrewLog;

public class BrewLogTest {

    String brewlogResponse = "{\n" +
            "    \"name\": \"08/05/2019\",\n" +
            "    \"originalRecipe\": {\n" +
            "        \"Id\": \"000000000000000000000000\",\n" +
            "        \"idString\": \"000000000000000000000000\",\n" +
            "        \"name\": \"Lilly Willy\",\n" +
            "        \"lastModifiedGuid\": \"ec12bffd-3473-4dc8-bddd-e00b49559d2a\",\n" +
            "        \"createdByUserID\": null,\n" +
            "        \"groupID\": null,\n" +
            "        \"isGroupEditable\": false,\n" +
            "        \"isPublic\": false,\n" +
            "        \"deleted\": false,\n" +
            "        \"description\": null,\n" +
            "        \"recipeStats\": {\n" +
            "            \"abv\": 62.994972229003906,\n" +
            "            \"ibu\": 27.552542721168717,\n" +
            "            \"fg\": 1.092455609090909,\n" +
            "            \"og\": 1.3488890909090909,\n" +
            "            \"srm\": 512.93096923828125,\n" +
            "            \"initialStrikeWaterTemp\": 168.6,\n" +
            "            \"initialStrikeWaterVolume\": 108.75\n" +
            "        },\n" +
            "        \"recipeParameters\": {\n" +
            "            \"ibuCalcType\": \"basic\",\n" +
            "            \"fermentableCalcType\": \"basic\",\n" +
            "            \"ibuBoilTimeCurveFit\": -0.04,\n" +
            "            \"gristRatio\": 1.25,\n" +
            "            \"initialMashTemp\": 70,\n" +
            "            \"targetMashTemp\": 155\n" +
            "        },\n" +
            "        \"version\": 0,\n" +
            "        \"clonedFrom\": null,\n" +
            "        \"hidden\": null,\n" +
            "        \"hops\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"d9068598-bbbd-4887-aa7a-987580e9901f\",\n" +
            "                \"amount\": 15,\n" +
            "                \"type\": \"Boil\",\n" +
            "                \"time\": 60,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc4054dd7744535d8b6\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Brewers Gold\",\n" +
            "                    \"aau\": 7.6,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"b57223a5-6df4-40e7-a42e-50fcf7dc1ff7\",\n" +
            "                \"amount\": 1,\n" +
            "                \"type\": \"Whirlpool\",\n" +
            "                \"time\": 0,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc5054dd7744535d8bc\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Chinook\",\n" +
            "                    \"aau\": 13,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"ea4e1005-3e6c-4ba3-884b-1a7577125e06\",\n" +
            "                \"amount\": 1,\n" +
            "                \"type\": \"Boil\",\n" +
            "                \"time\": 50,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc4054dd7744535d8b5\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Bravo\",\n" +
            "                    \"aau\": 15.5,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"9d1bbc7e-d80c-4110-99f3-b45cf334291a\",\n" +
            "                \"amount\": 0,\n" +
            "                \"type\": \"Boil\",\n" +
            "                \"time\": 0,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc4054dd7744535d8b3\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Bor (CZ)\",\n" +
            "                    \"aau\": 8,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"fermentables\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"54527cc1-e196-43db-9b1a-9adc85cee452\",\n" +
            "                \"weight\": 24,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Briess - 2 Row Brewers Malt\",\n" +
            "                    \"yield\": 80.5,\n" +
            "                    \"color\": 1.7999999523162842,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"Briess\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"225df642-1895-4fd1-b652-3f7fd7c255bb\",\n" +
            "                \"weight\": 52,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Black (Patent) Malt\",\n" +
            "                    \"yield\": 55,\n" +
            "                    \"color\": 500,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"f969ae43-97c7-44c9-bf97-011e5f878209\",\n" +
            "                \"weight\": 11,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Barley, Flaked\",\n" +
            "                    \"yield\": 70,\n" +
            "                    \"color\": 2,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"3a778729-46c6-4fa9-af40-10ef948beddf\",\n" +
            "                \"weight\": 0,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Barley, Raw\",\n" +
            "                    \"yield\": 60.900001525878906,\n" +
            "                    \"color\": 2,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"yeasts\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"ea2c3c32-d9e8-48cf-9cb7-11c6bde3dfa1\",\n" +
            "                \"yeast\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"WLP004 - Irish Ale Yeast\",\n" +
            "                    \"lab\": \"White Labs\",\n" +
            "                    \"attenuation\": 71,\n" +
            "                    \"beertype\": null,\n" +
            "                    \"form\": null,\n" +
            "                    \"minTemperature\": 0,\n" +
            "                    \"maxTemperature\": 0,\n" +
            "                    \"flocculation\": null,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"72d394d1-495f-4712-8018-27ca6195d1b6\",\n" +
            "                \"yeast\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"WLP006 - Bedford British\",\n" +
            "                    \"lab\": \"White Labs\",\n" +
            "                    \"attenuation\": 76,\n" +
            "                    \"beertype\": null,\n" +
            "                    \"form\": null,\n" +
            "                    \"minTemperature\": 0,\n" +
            "                    \"maxTemperature\": 0,\n" +
            "                    \"flocculation\": null,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"adjuncts\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"9bda43c6-5ce2-4cd0-8209-a0f5e6555a16\",\n" +
            "                \"amount\": 0,\n" +
            "                \"unit\": \"\",\n" +
            "                \"time\": 0,\n" +
            "                \"timeUnit\": \"\",\n" +
            "                \"type\": \"\",\n" +
            "                \"adjunct\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Dirt 2\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"8ee08c88-ab31-4330-838b-7b0849a06123\",\n" +
            "                \"amount\": 0,\n" +
            "                \"unit\": \"\",\n" +
            "                \"time\": 0,\n" +
            "                \"timeUnit\": \"\",\n" +
            "                \"type\": \"\",\n" +
            "                \"adjunct\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Dirt 2\"\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"style\": {\n" +
            "            \"idString\": \"5d37799d054dd7744535d98b\",\n" +
            "            \"name\": \"Baltic Porter\",\n" +
            "            \"category\": \"Porter\",\n" +
            "            \"description\": \"May also be described as an Imperial Porter, although heavily roasted or hopped versions should be entered as either Imperial Stouts (13F) or Specialty Beers (23).\",\n" +
            "            \"profile\": \"Rich malty sweetness often containing caramel, toffee, nutty to deep toast, and/or licorice notes. Complex alcohol and ester profile of moderate strength, and reminiscent of plums, prunes, raisins, cherries or currants, occasionally with a vinous Port-like quality. Some darker malt character that is deep chocolate, coffee or molasses but never burnt. No hops. No sourness. Very smooth.Dark reddish copper to opaque dark brown (not black). Thick, persistent tan-colored head. Clear, although darker versions can be opaque.As with aroma, has a rich malty sweetness with a complex blend of deep malt, dried fruit esters, and alcohol. Has a prominent yet smooth schwarzbier-like roasted flavor that stops short of burnt. Mouth-filling and very smooth. Clean lager character; no diacetyl. Starts sweet but darker malt flavors quickly dominates and persists through finish. Just a touch dry with a hint of roast coffee or licorice in the finish. Malt can have a caramel, toffee, nutty, molasses and/or licorice complexity. Light hints of black currant and dark fruits. Medium-low to medium bitterness from malt and hops, just to provide balance. Hop flavor from slightly spicy hops (Lublin or Saaz types) ranges from none to medium-low.Generally quite full-bodied and smooth, with a well-aged alcohol warmth (although the rarer lower gravity Carnegie-style versions will have a medium body and less warmth). Medium to medium-high carbonation, making it seem even more mouth-filling. Not heavy on the tongue due to carbonation level. Most versions are in the 7-8.5% ABV range.A Baltic Porter often has the malt flavors reminiscent of an English brown porter and the restrained roast of a schwarzbier, but with a higher OG and alcohol content than either. Very complex, with multi-layered flavors. Traditional beer from countries bordering the Baltic Sea. Derived from English porters but influenced by Russian Imperial Stouts.\",\n" +
            "            \"ingredients\": \"Generally lager yeast (cold fermented if using ale yeast). Debittered chocolate or black malt. Munich or Vienna base malt. Continental hops. May contain crystal malts and/or adjuncts. Brown or amber malt common in historical recipes.\",\n" +
            "            \"examples\": \"Sinebrychoff Porter (Finland), Okocim Porter (Poland), Zywiec Porter (Poland), Baltika #6 Porter (Russia), Carnegie Stark Porter (Sweden), Aldaris Porteris (Latvia), Utenos Porter (Lithuania), Stepan Razin Porter (Russia), Nøgne ø porter (Norway), Neuzeller Kloster-Bräu Neuzeller Porter (Germany), Southampton Imperial Baltic Porter\",\n" +
            "            \"minOG\": 1.06,\n" +
            "            \"maxOG\": 1.09,\n" +
            "            \"minFG\": 1.016,\n" +
            "            \"maxFG\": 1.024,\n" +
            "            \"minIBU\": 20,\n" +
            "            \"maxIBU\": 40,\n" +
            "            \"minCarb\": 0,\n" +
            "            \"maxCarb\": 0,\n" +
            "            \"minColor\": 17,\n" +
            "            \"maxColor\": 30,\n" +
            "            \"minABV\": 5.5,\n" +
            "            \"maxABV\": 9.5\n" +
            "        },\n" +
            "        \"boilVolume\": 0,\n" +
            "        \"equipmentProfile\": {\n" +
            "            \"createdByUserId\": null,\n" +
            "            \"name\": \"Empty\",\n" +
            "            \"idString\": null,\n" +
            "            \"boilSize\": 6,\n" +
            "            \"intoFermenterVolume\": 5.5,\n" +
            "            \"efficiency\": 75,\n" +
            "            \"batchSize\": 0\n" +
            "        }\n" +
            "    },\n" +
            "    \"rectifiedRecipe\": {\n" +
            "        \"Id\": \"000000000000000000000000\",\n" +
            "        \"idString\": \"000000000000000000000000\",\n" +
            "        \"name\": \"Lilly Willy\",\n" +
            "        \"lastModifiedGuid\": \"ec12bffd-3473-4dc8-bddd-e00b49559d2a\",\n" +
            "        \"createdByUserID\": null,\n" +
            "        \"groupID\": null,\n" +
            "        \"isGroupEditable\": false,\n" +
            "        \"isPublic\": false,\n" +
            "        \"deleted\": false,\n" +
            "        \"description\": null,\n" +
            "        \"recipeStats\": {\n" +
            "            \"abv\": 62.994972229003906,\n" +
            "            \"ibu\": 27.552542721168717,\n" +
            "            \"fg\": 1.092455609090909,\n" +
            "            \"og\": 1.3488890909090909,\n" +
            "            \"srm\": 512.93096923828125,\n" +
            "            \"initialStrikeWaterTemp\": 168.6,\n" +
            "            \"initialStrikeWaterVolume\": 108.75\n" +
            "        },\n" +
            "        \"recipeParameters\": {\n" +
            "            \"ibuCalcType\": \"basic\",\n" +
            "            \"fermentableCalcType\": \"basic\",\n" +
            "            \"ibuBoilTimeCurveFit\": -0.04,\n" +
            "            \"gristRatio\": 1.25,\n" +
            "            \"initialMashTemp\": 70,\n" +
            "            \"targetMashTemp\": 155\n" +
            "        },\n" +
            "        \"version\": 0,\n" +
            "        \"clonedFrom\": null,\n" +
            "        \"hidden\": null,\n" +
            "        \"hops\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"d9068598-bbbd-4887-aa7a-987580e9901f\",\n" +
            "                \"amount\": 15,\n" +
            "                \"type\": \"Boil\",\n" +
            "                \"time\": 60,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc4054dd7744535d8b6\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Brewers Gold\",\n" +
            "                    \"aau\": 7.6,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"b57223a5-6df4-40e7-a42e-50fcf7dc1ff7\",\n" +
            "                \"amount\": 1,\n" +
            "                \"type\": \"Whirlpool\",\n" +
            "                \"time\": 0,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc5054dd7744535d8bc\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Chinook\",\n" +
            "                    \"aau\": 13,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"ea4e1005-3e6c-4ba3-884b-1a7577125e06\",\n" +
            "                \"amount\": 1,\n" +
            "                \"type\": \"Boil\",\n" +
            "                \"time\": 50,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc4054dd7744535d8b5\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Bravo\",\n" +
            "                    \"aau\": 15.5,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"9d1bbc7e-d80c-4110-99f3-b45cf334291a\",\n" +
            "                \"amount\": 0,\n" +
            "                \"type\": \"Boil\",\n" +
            "                \"time\": 0,\n" +
            "                \"hop\": {\n" +
            "                    \"idString\": \"5d328cc4054dd7744535d8b3\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Bor (CZ)\",\n" +
            "                    \"aau\": 8,\n" +
            "                    \"beta\": 0,\n" +
            "                    \"notes\": null,\n" +
            "                    \"type\": null,\n" +
            "                    \"hsi\": 0,\n" +
            "                    \"origin\": null,\n" +
            "                    \"substitutes\": null,\n" +
            "                    \"humulene\": 0,\n" +
            "                    \"caryophyllene\": 0,\n" +
            "                    \"cohumulone\": 0,\n" +
            "                    \"myrcene\": 0\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"fermentables\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"54527cc1-e196-43db-9b1a-9adc85cee452\",\n" +
            "                \"weight\": 24,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Briess - 2 Row Brewers Malt\",\n" +
            "                    \"yield\": 80.5,\n" +
            "                    \"color\": 1.7999999523162842,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"Briess\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"225df642-1895-4fd1-b652-3f7fd7c255bb\",\n" +
            "                \"weight\": 52,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Black (Patent) Malt\",\n" +
            "                    \"yield\": 55,\n" +
            "                    \"color\": 500,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"f969ae43-97c7-44c9-bf97-011e5f878209\",\n" +
            "                \"weight\": 11,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Barley, Flaked\",\n" +
            "                    \"yield\": 70,\n" +
            "                    \"color\": 2,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"3a778729-46c6-4fa9-af40-10ef948beddf\",\n" +
            "                \"weight\": 0,\n" +
            "                \"fermentable\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Barley, Raw\",\n" +
            "                    \"yield\": 60.900001525878906,\n" +
            "                    \"color\": 2,\n" +
            "                    \"type\": \"Grain\",\n" +
            "                    \"maltster\": \"\",\n" +
            "                    \"origin\": null,\n" +
            "                    \"coarse_fine_diff\": 0,\n" +
            "                    \"moisture\": 0,\n" +
            "                    \"diastatic_power\": 0,\n" +
            "                    \"protein\": 0,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"yeasts\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"ea2c3c32-d9e8-48cf-9cb7-11c6bde3dfa1\",\n" +
            "                \"yeast\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"WLP004 - Irish Ale Yeast\",\n" +
            "                    \"lab\": \"White Labs\",\n" +
            "                    \"attenuation\": 71,\n" +
            "                    \"beertype\": null,\n" +
            "                    \"form\": null,\n" +
            "                    \"minTemperature\": 0,\n" +
            "                    \"maxTemperature\": 0,\n" +
            "                    \"flocculation\": null,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"72d394d1-495f-4712-8018-27ca6195d1b6\",\n" +
            "                \"yeast\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"WLP006 - Bedford British\",\n" +
            "                    \"lab\": \"White Labs\",\n" +
            "                    \"attenuation\": 76,\n" +
            "                    \"beertype\": null,\n" +
            "                    \"form\": null,\n" +
            "                    \"minTemperature\": 0,\n" +
            "                    \"maxTemperature\": 0,\n" +
            "                    \"flocculation\": null,\n" +
            "                    \"notes\": null\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"adjuncts\": [\n" +
            "            {\n" +
            "                \"additionGuid\": \"9bda43c6-5ce2-4cd0-8209-a0f5e6555a16\",\n" +
            "                \"amount\": 0,\n" +
            "                \"unit\": \"\",\n" +
            "                \"time\": 0,\n" +
            "                \"timeUnit\": \"\",\n" +
            "                \"type\": \"\",\n" +
            "                \"adjunct\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Dirt 2\"\n" +
            "                }\n" +
            "            },\n" +
            "            {\n" +
            "                \"additionGuid\": \"8ee08c88-ab31-4330-838b-7b0849a06123\",\n" +
            "                \"amount\": 0,\n" +
            "                \"unit\": \"\",\n" +
            "                \"time\": 0,\n" +
            "                \"timeUnit\": \"\",\n" +
            "                \"type\": \"\",\n" +
            "                \"adjunct\": {\n" +
            "                    \"Id\": \"000000000000000000000000\",\n" +
            "                    \"idString\": \"000000000000000000000000\",\n" +
            "                    \"createdByUserId\": null,\n" +
            "                    \"name\": \"Dirt 2\"\n" +
            "                }\n" +
            "            }\n" +
            "        ],\n" +
            "        \"style\": {\n" +
            "            \"idString\": \"5d37799d054dd7744535d98b\",\n" +
            "            \"name\": \"Baltic Porter\",\n" +
            "            \"category\": \"Porter\",\n" +
            "            \"description\": \"May also be described as an Imperial Porter, although heavily roasted or hopped versions should be entered as either Imperial Stouts (13F) or Specialty Beers (23).\",\n" +
            "            \"profile\": \"Rich malty sweetness often containing caramel, toffee, nutty to deep toast, and/or licorice notes. Complex alcohol and ester profile of moderate strength, and reminiscent of plums, prunes, raisins, cherries or currants, occasionally with a vinous Port-like quality. Some darker malt character that is deep chocolate, coffee or molasses but never burnt. No hops. No sourness. Very smooth.Dark reddish copper to opaque dark brown (not black). Thick, persistent tan-colored head. Clear, although darker versions can be opaque.As with aroma, has a rich malty sweetness with a complex blend of deep malt, dried fruit esters, and alcohol. Has a prominent yet smooth schwarzbier-like roasted flavor that stops short of burnt. Mouth-filling and very smooth. Clean lager character; no diacetyl. Starts sweet but darker malt flavors quickly dominates and persists through finish. Just a touch dry with a hint of roast coffee or licorice in the finish. Malt can have a caramel, toffee, nutty, molasses and/or licorice complexity. Light hints of black currant and dark fruits. Medium-low to medium bitterness from malt and hops, just to provide balance. Hop flavor from slightly spicy hops (Lublin or Saaz types) ranges from none to medium-low.Generally quite full-bodied and smooth, with a well-aged alcohol warmth (although the rarer lower gravity Carnegie-style versions will have a medium body and less warmth). Medium to medium-high carbonation, making it seem even more mouth-filling. Not heavy on the tongue due to carbonation level. Most versions are in the 7-8.5% ABV range.A Baltic Porter often has the malt flavors reminiscent of an English brown porter and the restrained roast of a schwarzbier, but with a higher OG and alcohol content than either. Very complex, with multi-layered flavors. Traditional beer from countries bordering the Baltic Sea. Derived from English porters but influenced by Russian Imperial Stouts.\",\n" +
            "            \"ingredients\": \"Generally lager yeast (cold fermented if using ale yeast). Debittered chocolate or black malt. Munich or Vienna base malt. Continental hops. May contain crystal malts and/or adjuncts. Brown or amber malt common in historical recipes.\",\n" +
            "            \"examples\": \"Sinebrychoff Porter (Finland), Okocim Porter (Poland), Zywiec Porter (Poland), Baltika #6 Porter (Russia), Carnegie Stark Porter (Sweden), Aldaris Porteris (Latvia), Utenos Porter (Lithuania), Stepan Razin Porter (Russia), Nøgne ø porter (Norway), Neuzeller Kloster-Bräu Neuzeller Porter (Germany), Southampton Imperial Baltic Porter\",\n" +
            "            \"minOG\": 1.06,\n" +
            "            \"maxOG\": 1.09,\n" +
            "            \"minFG\": 1.016,\n" +
            "            \"maxFG\": 1.024,\n" +
            "            \"minIBU\": 20,\n" +
            "            \"maxIBU\": 40,\n" +
            "            \"minCarb\": 0,\n" +
            "            \"maxCarb\": 0,\n" +
            "            \"minColor\": 17,\n" +
            "            \"maxColor\": 30,\n" +
            "            \"minABV\": 5.5,\n" +
            "            \"maxABV\": 9.5\n" +
            "        },\n" +
            "        \"boilVolume\": 0,\n" +
            "        \"equipmentProfile\": {\n" +
            "            \"createdByUserId\": null,\n" +
            "            \"name\": \"Empty\",\n" +
            "            \"idString\": null,\n" +
            "            \"boilSize\": 6,\n" +
            "            \"intoFermenterVolume\": 5.5,\n" +
            "            \"efficiency\": 75,\n" +
            "            \"batchSize\": 0\n" +
            "        }\n" +
            "    },\n" +
            "    \"mashStartTime\": \"0001-01-01T00:00:00+00:00\",\n" +
            "    \"mashEndTime\": \"0001-01-01T00:00:00+00:00\",\n" +
            "    \"vaurloff\": false,\n" +
            "    \"spargeStartTime\": \"0001-01-01T00:00:00+00:00\",\n" +
            "    \"spargeEndTime\": \"0001-01-01T00:00:00+00:00\",\n" +
            "    \"preBoilVolumeEstimate\": null,\n" +
            "    \"preBoilVolumeActual\": 0,\n" +
            "    \"boilStartTime\": \"0001-01-01T00:00:00+00:00\",\n" +
            "    \"boilEndTime\": \"0001-01-01T00:00:00+00:00\",\n" +
            "    \"actualHopAdditions\": null,\n" +
            "    \"actualAdjunctAdditions\": null,\n" +
            "    \"og\": 0,\n" +
            "    \"fg\": 0,\n" +
            "    \"actualBatchSizeString\": null,\n" +
            "    \"actualBatchSize\": 0,\n" +
            "    \"lastModifiedGuid\": \"11f843f6-caad-4cee-ae55-be438340b7a0\",\n" +
            "    \"Id\": \"5d48c4a56b28746bd820ddd5\",\n" +
            "    \"idString\": \"5d48c4a56b28746bd820ddd5\"\n" +
            "}";

    @Test
    public void ConvertBrewLogJsonToObject() {
        try {
            BrewLog brewLog = Tools.convertJsonResponseToObject(brewlogResponse, BrewLog.class);
        } catch (Exception e) {
            Assert.fail(e.getMessage());
            return;
        }

        Assert.assertTrue(true);
    }
}
