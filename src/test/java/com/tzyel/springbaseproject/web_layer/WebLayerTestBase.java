package com.tzyel.springbaseproject.web_layer;

import com.tzyel.springbaseproject.UnitTestConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ActiveProfiles("test")
@ContextConfiguration(classes = UnitTestConfiguration.class)
public class WebLayerTestBase {
}
