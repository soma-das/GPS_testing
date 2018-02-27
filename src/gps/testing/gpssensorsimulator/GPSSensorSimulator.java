package gps.testing.gpssensorsimulator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.bitreactive.examples.amqp.gpssensorsimulator.Coordinate;

import no.ntnu.item.arctis.runtime.Block;
import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LinearRing;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import de.micromata.opengis.kml.v_2_2_0.Polygon;

public class GPSSensorSimulator extends Block {
	public List<Coordinate> route;
	private int index = 0;
	private boolean forward = true;

	public List<Coordinate> convertRouteFromKML(Kml kml) {
		ArrayList<Coordinate> list = new ArrayList<Coordinate>();
		if (kml.getFeature() instanceof Document) {
			Document d = (Document) kml.getFeature();
			for (Feature f : d.getFeature()) {
				if (f instanceof Placemark) {
					Placemark p = (Placemark) f;
					if (p.getGeometry() instanceof Polygon) {
						Polygon pg = (Polygon) p.getGeometry();
						LinearRing lr = pg.getOuterBoundaryIs().getLinearRing();
						for (de.micromata.opengis.kml.v_2_2_0.Coordinate c : lr.getCoordinates()) {
							Coordinate cn = new Coordinate();
							cn.latitude = (float) c.getLatitude();
							cn.longitude = (float) c.getLongitude();
							list.add(cn);
						}
						return list;
					} else if (p.getGeometry() instanceof LineString) {
						LineString pg = (LineString) p.getGeometry();
						for (de.micromata.opengis.kml.v_2_2_0.Coordinate c : pg.getCoordinates()) {
							Coordinate cn = new Coordinate();
							cn.latitude = (float) c.getLatitude();
							cn.longitude = (float) c.getLongitude();
							list.add(cn);
						}
						return list;
					}
				}
			}
		}
		logger.error("Failed to read list of coordinates from KML file!");
		return list;
	}

	public File getRouteFile() {
		return new File("files" + File.separator + "route.kml");
	}

	public Coordinate getNextPosition() {
		Coordinate next = route.get(index);
		if (forward && index == route.size() - 1) {
			forward = false;
		} else if (!forward && index == 0) {
			forward = true;
		} else if (forward) {
			++index;
		} else if (!forward) {
			--index;
		}
		return next;
	}
}
