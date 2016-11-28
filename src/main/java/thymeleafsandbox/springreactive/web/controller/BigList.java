/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2014, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package thymeleafsandbox.springreactive.web.controller;

import java.util.List;

import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.thymeleaf.spring5.context.reactive.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import thymeleafsandbox.springreactive.business.PlaylistEntry;
import thymeleafsandbox.springreactive.business.repository.PlaylistEntryRepository;


@Controller
public class BigList {


    @Autowired
    private PlaylistEntryRepository playlistEntryRepository;



    public BigList() {
        super();
    }




    @RequestMapping("/biglist-datadriven.thymeleaf")
    public String dataDrivenFlowUnbufferedThymeleaf(final Model model) {

        final Publisher<PlaylistEntry> playlistFlow = this.playlistEntryRepository.findLargeCollectionPlaylistEntries();
        // No need to fully resolve the Publisher! We will just let it drive
        model.addAttribute("dataSource", new ReactiveDataDriverContextVariable<>(playlistFlow, 1000));

        return "thymeleaf/biglist-datadriven";

    }


    @RequestMapping("/biglist-buffered.thymeleaf")
    public String bigListBufferedThymeleaf(final Model model) {

        final Publisher<PlaylistEntry> playlistFlow = this.playlistEntryRepository.findLargeCollectionPlaylistEntries();
        // We need to fully resolve the list before executing the template
        final List<PlaylistEntry> playlistEntries = Flux.from(playlistFlow).collectList().block();

        model.addAttribute("dataSource", playlistEntries);

        return "thymeleaf/biglist-buffered";

    }


    @RequestMapping("/biglist-normal.thymeleaf")
    public String bigListNormalThymeleaf(final Model model) {

        final Publisher<PlaylistEntry> playlistFlow = this.playlistEntryRepository.findLargeCollectionPlaylistEntries();
        // We need to fully resolve the list before executing the template
        final List<PlaylistEntry> playlistEntries = Flux.from(playlistFlow).collectList().block();

        model.addAttribute("dataSource", playlistEntries);

        return "thymeleaf/biglist-normal";

    }


    @RequestMapping("/biglist.freemarker")
    public String bigListFreeMarker(final Model model) {

        final Publisher<PlaylistEntry> playlistFlow = this.playlistEntryRepository.findLargeCollectionPlaylistEntries();
        // We need to fully resolve the list before executing the template
        final List<PlaylistEntry> playlistEntries = Flux.from(playlistFlow).collectList().block();

        model.addAttribute("dataSource", playlistEntries);

        return "freemarker/biglist";

    }

}
