/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tech.fate.portal.constants;

import com.google.common.collect.Lists;

import java.util.List;

public class NodeConstants {
    public final static List<String> CONTAINS_MODEL_NODELIST = Lists.newArrayList("data_transform", "feature_scale", "hetero_feature_binning", "hetero_feature_selection", "one_hot_encoder", "hetero_lr");

    public final static List<String> ARBITER_MUST_BE_HOST_NODE = Lists.newArrayList("hetero_lr", "heterolinr", "heteropoisson");
}
