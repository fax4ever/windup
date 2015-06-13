package org.jboss.windup.config.parser.metadata;

import org.apache.commons.lang.StringUtils;
import org.jboss.forge.furnace.versions.VersionException;
import org.jboss.forge.furnace.versions.VersionRange;
import org.jboss.forge.furnace.versions.Versions;
import org.jboss.windup.config.exception.ConfigurationException;
import org.jboss.windup.config.metadata.TechnologyReference;
import org.jboss.windup.config.parser.ElementHandler;
import org.jboss.windup.config.parser.ParserContext;
import org.jboss.windup.config.parser.WindupXMLRulesetParsingException;
import org.w3c.dom.Element;

/**
 * Provides support for parsing a technology id and version range into a {@link TechnologyReference} object.
 *
 * @author <a href="mailto:jesse.sightler@gmail.com">Jesse Sightler</a>
 */
public abstract class AbstractMetadataTechnologyHandler implements ElementHandler<TechnologyReference>
{
    private static String ID = "id";
    private static String VERSION_RANGE = "versionRange";

    @Override
    public TechnologyReference processElement(ParserContext handlerManager, Element element) throws ConfigurationException
    {
        String id = element.getAttribute(ID);
        String versionRangeString = element.getAttribute(VERSION_RANGE);
        if (StringUtils.isBlank(id))
        {
            throw new WindupXMLRulesetParsingException("The '" + element.getTagName() + "' element must have a non-empty '" + ID + "' attribute");
        }
        VersionRange versionRange = null;
        if (StringUtils.isNotBlank(versionRangeString))
        {
            try
            {
                versionRange = Versions.parseVersionRange(versionRangeString);
            }
            catch (VersionException ex)
            {
                throw new WindupXMLRulesetParsingException("The '" + VERSION_RANGE + "' attribute with value \"" + versionRangeString
                            + "\" in the element "
                            + element.getTagName() + " is not a valid version", ex);
            }
        }

        TechnologyReference reference = new TechnologyReference(id, versionRange);
        return reference;
    }
}