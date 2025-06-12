/*
 * Copyright (c) 2025 Riege Software. All rights reserved.
 * Use is subject to license terms.
 */

package org.acme;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithName;

import java.util.Map;
import java.util.Optional;

@ConfigMapping(prefix = "demo")
interface DemoConfig {

    @WithName("inner")
    Map<String, Inner> inners();

    interface Inner {
        String id();

        Optional<String> data();
    }
}
