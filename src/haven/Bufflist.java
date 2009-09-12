/*
 *  This file is part of the Haven & Hearth game client.
 *  Copyright (C) 2009 Fredrik Tolf <fredrik@dolda2000.com>, and
 *                     Björn Johannessen <johannessen.bjorn@gmail.com>
 *
 *  Redistribution and/or modification of this file is subject to the
 *  terms of the GNU Lesser General Public License, version 3, as
 *  published by the Free Software Foundation.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  Other parts of this source tree adhere to other copying
 *  rights. Please see the file `COPYING' in the root directory of the
 *  source tree for details.
 *
 *  A copy the GNU Lesser General Public License is distributed along
 *  with the source tree of which this file is a part in the file
 *  `doc/LPGL-3'. If it is missing for any reason, please see the Free
 *  Software Foundation's website at <http://www.fsf.org/>, or write
 *  to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 *  Boston, MA 02111-1307 USA
 */

package haven;

import java.awt.Color;

public class Bufflist extends Widget {
    static Tex frame = Resource.loadtex("gfx/hud/buffs/frame");
    static Tex cframe = Resource.loadtex("gfx/hud/buffs/cframe");
    static final Coord imgoff = new Coord(3, 3);
    static final Coord ameteroff = new Coord(3, 36);
    static final Coord ametersz = new Coord(30, 2);
    static final int margin = 2;
    static final int num = 5;
    
    static {
        Widget.addtype("buffs", new WidgetFactory() {
            public Widget create(Coord c, Widget parent, Object[] args) {
                return(new Bufflist(c, parent));
            }
        });
    }
    
    public Bufflist(Coord c, Widget parent) {
	super(c, new Coord((num * frame.sz().x) + ((num - 1) * margin), cframe.sz().y), parent);
    }
    
    public void draw(GOut g) {
	int i = 0;
	int w = frame.sz().x + margin;
	long now = System.currentTimeMillis();
	for(Buff b : ui.sess.glob.buffs.values()) {
	    if(b.ameter >= 0) {
		g.image(cframe, new Coord(i * w, 0));
		g.chcolor(Color.BLACK);
		g.frect(ameteroff, ametersz);
		g.chcolor(Color.WHITE);
		g.frect(ameteroff, new Coord((b.ameter * ametersz.x) / 100, ametersz.y));
		g.chcolor();
	    } else {
		g.image(frame, new Coord(i * w, 0));
	    }
	    if(b.res.get() != null) {
		Tex img = b.res.get().layer(Resource.imgc).tex();
		g.image(img, new Coord(i * w, 0).add(imgoff));
		if(b.nmeter >= 0) {
		    Tex ntext = b.nmeter();
		    g.image(ntext, imgoff.add(img.sz()).add(ntext.sz().inv()).add(-1, -1));
		}
		if(b.cmeter >= 0) {
		    double m = b.cmeter / 100.0;
		    if(b.cticks >= 0) {
			double ot = b.cticks * 0.06;
			double pt = ((double)(now - b.gettime)) / 1000.0;
			m *= 1.0 - ((ot - pt) / ot);
		    }
		    g.chcolor(255, 255, 255, 64);
		    g.fellipse(imgoff.add(img.sz().div(2)), img.sz().div(2), (int)(90 - (360 * m)), 90);
		    g.chcolor();
		}
	    }
	}
    }
}